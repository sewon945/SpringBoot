package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.jwt.JwtUtil;
import com.example.demo.model.LoginDTO;
import com.example.demo.model.Users;
import com.example.demo.service.UserDetailService;
import com.example.demo.service.UserService;

@RestController
public class UserRestController {
	// 리엑트 사용할 때 쓰는 클래스
	
	@Autowired
	private UserService service;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserDetailService detailService;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@PostMapping("/api/member/join")
	public ResponseEntity<String> join(Users users) {
		
		service.join(users);
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}
	
	@PostMapping("/api/member/login")
	public ResponseEntity<String> login(LoginDTO dto) {
		// Manager : 권한 인증 처리
		authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
            );
		// 유저 정보 token에 가져옴
        final UserDetails userDetails = detailService.loadUserByUsername(dto.getEmail());
        String token = jwtUtil.createAccessToken(userDetails);

        return ResponseEntity.status(HttpStatus.OK).body(token);
	}
}
