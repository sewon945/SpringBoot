package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.example.demo.jwt.CustomAuthenticationEntryPoint;
import com.example.demo.jwt.JwtAuthenticationFilter;
import com.example.demo.jwt.JwtExceptionFilter;
import com.example.demo.jwt.JwtUtil;
import com.example.demo.service.UserDetailService;

@Configuration // 설정파일
@EnableWebSecurity // Spring Security 활성화
               // 내부적으로 SpringSecurityFilterChain 동작

public class SecurityConfig {
   
   @Autowired
   private UserDetailService userDetailService;
   
   
   @Autowired
   private JwtUtil jwtUtil;
   
   // css , js , 이미지 (정적리소스) => 정적리소스가 제대로 적용되지 않는 상황 발생
   @Bean // 수동으로 객체 생성
   public WebSecurityCustomizer webSecurityCustomizer() {
      return (web) -> web.ignoring().requestMatchers("/static/**");
   }
   
   @Bean
   public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
      http
      .csrf(csrf -> csrf.disable()) // GET 요청 제외 요청으로부터 보호, csrf 토큰 포함(위조요청), rest api 사용시에는 설정 X
      // HTTP 세션 관리 상태 없음으로 구성한 것(쓰지 않겠다고 구성)
      .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(
    		  SessionCreationPolicy.STATELESS))
      .formLogin(form -> form.disable())  // 로그인 폼 비활성화
      .httpBasic(AbstractHttpConfigurer::disable)  // HTTP 기반 기본 인증 비활성화
      // filter 사용할 때 첫 번째 자리(jwtfilter)부터 실행하겠다
      .addFilterBefore(new JwtAuthenticationFilter(userDetailService, jwtUtil), UsernamePasswordAuthenticationFilter.class)  
      // JwtExceptionFilter()부터 사용하겠다
      .addFilterBefore(new JwtExceptionFilter(), JwtAuthenticationFilter.class)
      .authorizeHttpRequests(auth -> auth
            // .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
            // .requestMatchers("/","/join","/login" , "/api/member/join").permitAll() // 인증 없이도 접근 가능한 경로 설정
            .requestMatchers("/api/member/*").permitAll() // 회원가입, 로그인은 인증 없이도 접근 가능한 경로 설정
            .requestMatchers("/admin").hasRole("ADMIN") // ADMIN 권한이 있을 경우에만 접근 가능한 경로 설정
            .anyRequest().authenticated() // 그 외의 요청은 무조권 인증 확인하겠다!
      )
      .exceptionHandling(exceptionHandling-> exceptionHandling
              .authenticationEntryPoint(new CustomAuthenticationEntryPoint()))
        ;
      
      // 내부 jsp를 사용하기 위해서 만들어놓은 것 - 기본 폼을 쓰지 않겠다고 지움(주석)
//      .formLogin(form -> form
//            .loginPage("/login") // 로그인 페이지 설정
//            .defaultSuccessUrl("/") // 로그인 성공 시 요청 url (index.jsp)
//            .failureUrl("/login") // 로그인 실패 시 요청 url (login.jsp)
//            .permitAll()
//      )
//      .logout(logout -> logout
//            .logoutUrl("/logout") // 로그아웃 url 설정
//            .logoutSuccessUrl("/login") // 로그아웃 후 url (login.jsp)
//            .permitAll()
//       )
//      .userDetailsService(userDetailService);
      
      return http.build();
   }
   
   @Bean
   public PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
   }
   
   // CORS - Cross-origin resource sharing (교차 출처 리소스 공유)
   // 도메인 밖의 다른 도메인으로부터 요청할 수 있게 허용  
   // CorsFilter : spring 있는 걸로 import
   @Bean
   public CorsFilter corsFilter() {
       CorsConfiguration config = new CorsConfiguration();
       config.setAllowCredentials(true);
       config.addAllowedOrigin("http://localhost:3002");// 리액트 서버(원래 선생님은 3000번인데 지금 나는 리액트 3개를 켜놔서 3002번)
       config.addAllowedHeader("*");
       config.addAllowedMethod("*");
       // UrlBasedCorsConfigurationSource : import 더 짧은 걸로
       UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
       source.registerCorsConfiguration("/**", config); //모든 경로에 대해 CORS 설정

       return new CorsFilter(source);
   }
   
   // 인증 공급자와 인증 관리자는 로그인 시 사용 => jwt(web token 사용) - 사용자를 식별할 수 있는 세션?
   
   // Provider(인증 공급자) : UserDetailsService, PasswordEncoder 활용 인증 논리 구현
   @Bean
   public AuthenticationProvider authenticationProvider() {
       DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
       authenticationProvider.setUserDetailsService(userDetailService);
       authenticationProvider.setPasswordEncoder(passwordEncoder());
       return authenticationProvider;

   }
   
   // 인증 관리자 : 인증 공급자를 활용해서 실제 인증을 처리해주는 것
   @Bean
   public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
       return config.getAuthenticationManager();
   }
   
}
