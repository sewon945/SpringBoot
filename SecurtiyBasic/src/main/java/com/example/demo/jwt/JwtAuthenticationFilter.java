package com.example.demo.jwt;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.service.UserDetailService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{
	// OncePerRequestFilter 상속받기
	// 받은 토큰을 가지고 인증하는 필터
	
	private UserDetailService detailService;
	private JwtUtil jwtUtil;
	
	public JwtAuthenticationFilter(UserDetailService detailService, JwtUtil jwtUtil) {
		this.detailService = detailService;
		this.jwtUtil = jwtUtil;
	}
	
	// JWT 토큰 검증하는 필터
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		// JWT 토큰 보내주기 - 리액트에서 getData 함수 보기 - 권한을 부여받은 사용자만 가능 ??
		String autorizationHeader = request.getHeader("Authorization");  // 토큰을 가지고 있음
		
		if(autorizationHeader != null && autorizationHeader.startsWith("Bearer ")) {
			// 둘 다 일치하면 토큰 가져오기
			String token = autorizationHeader.substring(7); // 7개 문자 자르기
			
			// token의 유효성 검증하기 - util에 만들어놓음
			// ture값을 받은 상태에서 검증하기
			if(jwtUtil.validateToken(token, request)) {
				
				// token에서 Claim 파싱 후 이메일만 반환
				String email = jwtUtil.getUserId(token);
				
				UserDetails userDetails = detailService.loadUserByUsername(email);
				
				if(userDetails != null) {  // 접근 가능한 토큰 발급 해주기
					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
					new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
					
					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				}
			}
			
		}
		
		filterChain.doFilter(request, response);
		
	}

}
