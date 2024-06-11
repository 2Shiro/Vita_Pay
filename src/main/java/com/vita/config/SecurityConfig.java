package com.vita.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.vita.oauth.jwt.JWTFilter;
import com.vita.oauth.jwt.JWTUtil;
import com.vita.oauth.mapper.RefreshMapper;
import com.vita.oauth.service.CustomOAuth2UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomSuccessHandler customSuccessHandler;
    private final JWTUtil jwtUtil;
    private final RefreshMapper refreshMapper;
    

    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService, CustomSuccessHandler customSuccessHandler,
    											JWTUtil jwtUtil, RefreshMapper refreshMapper) {

        this.customOAuth2UserService = customOAuth2UserService;
        this.customSuccessHandler = customSuccessHandler;
        this.jwtUtil = jwtUtil;
        this.refreshMapper =refreshMapper;
    }

 // 1. 스프링 시큐리티 기능 비활성화 (제외 설정)
    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring()
        		.requestMatchers("/")
        		.requestMatchers("/login")
        		.requestMatchers("/redirectHome")
                .requestMatchers("/css/**") 
                .requestMatchers("/js/**") 
                .requestMatchers("/img/**") // /static/**   : .html, .js, .css
                .requestMatchers("/WEB-INF/**")
                .requestMatchers("/token/retoken")
                .requestMatchers("/token/loginsuccess")
                .requestMatchers("/loginsuccess")
                .requestMatchers("/welcome");// /static/**   : .html, .js, .css
        //.requestMatchers("/static/**") 
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        //csrf disable
        http
                .csrf((auth) -> auth.disable());

        //From 로그인 방식 disable
        http
                .formLogin((auth) -> auth.disable());

        //HTTP Basic 인증 방식 disable
        http
                .httpBasic((auth) -> auth.disable());
        
        //JWTFilter 추가
		http
            .addFilterAfter(new JWTFilter(jwtUtil),OAuth2LoginAuthenticationFilter.class);
		
		 // CustomSuccessHandler를 UsernamePasswordAuthenticationFilter 자리에 추가
      

        //oauth2
        http
		        .oauth2Login((oauth2) -> oauth2
		                .userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig
		                        .userService(customOAuth2UserService))
		                		  .successHandler(customSuccessHandler)
		                );
		        

        //경로별 인가 작업
        http
        	
        
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/","/home.jsp","/home","/oauth2/**","/login/**","/login/login.jsp").permitAll()
                        .requestMatchers("/reissue","/redirectHome","/loginsuccess").permitAll()
                        .anyRequest().authenticated());
       
        //세션 설정 : STATELESS
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
