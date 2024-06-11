package com.vita.oauth.jwt;

import java.io.IOException;
import java.io.PrintWriter;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.vita.oauth.domain.CustomOAuth2User;
import com.vita.oauth.domain.UserDTO;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JWTFilter extends OncePerRequestFilter {

	private final JWTUtil jwtUtil;

	public JWTFilter(JWTUtil jwtUtil) {

		this.jwtUtil = jwtUtil;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		 String authorization = null;
	        Cookie[] cookies = request.getCookies();
	        for (Cookie cookie : cookies) {

	            System.out.println(cookie.getName());
	            if (cookie.getName().equals("access")) {

	                authorization = cookie.getValue();
	            }
	        }
	        System.out.println("authorization :" + authorization);
		 //토근이 없다면 다음 필터로 넘김 토큰이 필요없는 요청일 수도 있으니까 일단 넘김
		 if(authorization == null) {
			 System.out.println("토큰이 없다~");
			 filterChain.doFilter(request, response);
			 return;
		 }
		
		 //토큰 만료 여부 확인, 만료시 다음 필터로 넘기지 않음
		  try{
			  jwtUtil.isExpired(authorization);
			  System.out.println("이게 뭔 값이지토큰 소멸시간? : "+jwtUtil.isExpired(authorization));
		  }catch(ExpiredJwtException e) {
			  System.out.println("토큰 만료여부 체크중");
			  //respose body
			  PrintWriter writer = response.getWriter(); // 먼지모름
			  writer.print("access token expired");
			  
			  //respose status code
			  response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 프론트측에 상태코드를 주어서 리프레시토큰을 사용하게한다.
			  return;
		  }

		// 토큰이 access인지 확인 (발급시 페이로드에 명시)
		  String category = jwtUtil.getCategory(authorization);

		  System.out.println("카테고리까지옴");
		  if (!category.equals("access")) {
			  System.out.println("토큰값이 access인지 확인중");
		      //response body
		      PrintWriter writer = response.getWriter();
		      writer.print("invalid access token");

		      //response status code
		      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		      return;
		  }

		  

		// 토큰에서 username과 role 획득
		String oauth_name = jwtUtil.getUsername(authorization);
		String role = jwtUtil.getRole(authorization);
		Long user_id = jwtUtil.getUserId(authorization);

		// userDTO를 생성하여 값 set
		UserDTO userDTO = new UserDTO();
		userDTO.setUsername(oauth_name);
		userDTO.setRole(role);
		userDTO.setUserId(user_id);
		
		System.out.println("필터에서 userDto 값 저장하고 넘김: " + userDTO);

		// UserDetails에 회원 정보 객체 담기
		CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDTO);
		System.out.println("어디서 실행이안되1");
		// 스프링 시큐리티 인증 토큰 생성
		Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null,
				customOAuth2User.getAuthorities());
		System.out.println("어디서 실행이안되2");
		// 세션에 사용자 등록
		SecurityContextHolder.getContext().setAuthentication(authToken);
		System.out.println("세션에 사용자 등록됨");

		filterChain.doFilter(request, response);
	}
}