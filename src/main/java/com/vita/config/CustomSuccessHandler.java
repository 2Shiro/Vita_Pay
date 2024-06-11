package com.vita.config;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.vita.oauth.domain.CustomOAuth2User;
import com.vita.oauth.domain.RefreshDTO;
import com.vita.oauth.jwt.JWTUtil;
import com.vita.oauth.mapper.RefreshMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;
    private RefreshMapper refreshMapper;

    public CustomSuccessHandler(JWTUtil jwtUtil, RefreshMapper refreshMapper) {

        this.jwtUtil = jwtUtil;
        this.refreshMapper = refreshMapper;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

    	System.out.println("onAuthenticationSuccess");
        //OAuth2User
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();

        String username = customUserDetails.getUsername();
        Long userId = customUserDetails.getUserId();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

     
        //access토큰 
        String access = jwtUtil.createJwt("access", username, role, 6000000000000000000L, userId);
        System.out.println("access 값이 들어가나?"+access);
        String refresh = jwtUtil.createJwt("refresh", username, role, 86400000L, userId);
        System.out.println("refresh 값이 들어가나?"+refresh);
        
        addRefreshDTO(username, refresh, 86400000L, userId);
        
        //String redirectURL = "/loginsuccess?access=" + URLEncoder.encode(access, "UTF-8") + "&refresh=" + URLEncoder.encode(refresh, "UTF-8");
        //응답 설정
        String redirectURL = "/";
        
        response.addCookie(createCookie("access", access));
        response.addCookie(createCookie("refresh", refresh));
        response.addCookie(createCookie("userId", userId.toString()));
        response.setStatus(HttpStatus.OK.value());
        
        response.sendRedirect(redirectURL);
        // access 토큰을 쿼리 매개변수로 전달
		/*
		 * String redirectURL = request.getContextPath() + "/welcome?access=" +
		 * URLEncoder.encode(access, "UTF-8");
		 * 
		 * // 새로운 URL로 리디렉션 response.sendRedirect(redirectURL);
		 */
       // response.setHeader("Authorization", "Bearer" + token);
        //response.sendRedirect("/token/retoken");
    }

    private void addRefreshDTO(String username, String refresh, Long expiredMs, Long userId) {

        Date date = new Date(System.currentTimeMillis() + expiredMs);

        RefreshDTO refreshDto = new RefreshDTO();
        refreshDto.setUsername(username);
        refreshDto.setRefresh(refresh);
        refreshDto.setExpiration(date.toString());
        refreshDto.setId(userId);
        refreshMapper.save(refreshDto);
    }
    
	  private Cookie createCookie(String key, String value) {
	 
		 Cookie cookie = new Cookie(key, value);
		 cookie.setMaxAge(24*60*60);
		 //cookie.setSecure(true);//https 를 진행시 넣어야함
		 cookie.setPath("/"); //쿠키가 적용될 범위도 적을수 있음
		// cookie.setHttpOnly(true);//자바스크립트로 쿠키접근 막음
		 
		 return cookie; 
		 }
	 
}