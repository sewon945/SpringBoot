package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.repository.UserRepository;


@Service
public class UserDetailService implements UserDetailsService{

   @Autowired
   private UserRepository repository;
   
   // 사용자 인증 관련된 서비스(중요하게 쓰이고 있는 메서드)
   @Override
   public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      
      return repository.findByEmail(username)
              .orElseThrow(()->new UsernameNotFoundException("USER NOT FOUND WITH EMAIL : "+ username));
   }

}
