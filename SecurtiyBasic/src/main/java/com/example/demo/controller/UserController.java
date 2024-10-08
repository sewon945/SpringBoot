package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class UserController {
   
   @Autowired
   private UserService service;
   
   // jsp 쓸 때만 사용하기
//   @PostMapping("/api/member/join")
//   public void join(Users users) {
//      service.join(users);
//   }
   
   @GetMapping("/logout")
   public void logout(HttpServletRequest request, HttpServletResponse response) {
      // 인증객체삭제
      new SecurityContextLogoutHandler().logout(request, response , SecurityContextHolder.getContext().getAuthentication());
   }
}
