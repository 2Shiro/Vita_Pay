package com.vita.pay.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.vita.oauth.domain.CustomOAuth2User;
import com.vita.oauth.jwt.JWTUtil;

@Controller
public class PayController {
	 @Autowired
	 private JWTUtil jwtUtil;

	
	@GetMapping("/Pay/Basket")
	public String basket() {
		 System.out.println("페이뷰로옴~");
		 System.out.println("페이뷰로옴~");
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	        System.out.println("authentication은 오나 ? : " + authentication);

	        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();
	        Long userId = customOAuth2User.getUserId();

	        System.out.println("user_id: " + userId);
		
		return "pay/basket";
	}
	
	@GetMapping("/Pay/PayForm")
	public String payform() {
		 System.out.println("페이뷰로옴~");
		 System.out.println("페이뷰로옴~");
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	        System.out.println("authentication은 오나 ? : " + authentication);

	        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();
	        Long userId = customOAuth2User.getUserId();

	        System.out.println("user_id: " + userId);
		
		return "pay/pay";
	}

}
