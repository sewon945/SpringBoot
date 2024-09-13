package com.example.demo.jwt;

import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.example.demo.model.Users;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtUtil {  // jwt 역할 : 토큰 생성, 유효한 토큰, 사용자 정보 확인
	
	// 토큰의 유효 기간
	private long accessTokenExpTime;
	// 토큰의 키(씨큐리티의 키로 import)
	private Key key;  // security 사용할 때 사용할 (암호화된) Key
	
	// ExpTime(만료시간), 암호화 전에 key로 사용할 문자열 넣어줄 것임
	// jwt.secret을 문자열의 secretKey로 사용하겠다, jwt.expiration_time을 long형의 accessTokenExpTime 사용하겠다!
	public JwtUtil(@Value("${jwt.secret}") String secretKey, 
			@Value("${jwt.expiration_time}") long accessTokenExpTime) {
		// application.properties에서 jwt 설정해주고 오기
		
		this.accessTokenExpTime = accessTokenExpTime;
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		// byte배열 사용해서 key 바꿔주기
		// 우리가 사용해야 하는 key
		this.key = Keys.hmacShaKeyFor(keyBytes);  // HMAC 알고리즘 적용한 Key 객체 생성
	}
	
	
	public String createAccessToken(UserDetails userDetails) { //AccessToken 생성 : 만들어진 토큰을 반환
        return createToken(userDetails, accessTokenExpTime);
        // createAccessToken 호출될 때 UserDetails
        // 사용자 정보를 받아서 createToken할 때 넘겨줄 거임
        
    }
	
	
	private String createToken(UserDetails user, long expireTime) {
	       
	       // Claims : 정보는 담는 조각, 토큰 생성 시 사용할 정보를 담기 위함(아이디, 이메일, 역함)
	        Claims claims = Jwts.claims();
	        claims.put("memberId", ((Users) user).getId());  // Users에서 getter 받아오기
	        claims.put("email", user.getUsername());
	        claims.put("role", ((Users) user).getRole());   // Users에서 getter 받아오기

	        ZonedDateTime now = ZonedDateTime.now();  // 현재 시간 기준 실제 만료 날짜 구하기 위함
	        ZonedDateTime tokenValidity = now.plusSeconds(expireTime);


	        return Jwts.builder()
	                .setClaims(claims)
	                .setIssuedAt(Date.from(now.toInstant()))  // 현재 시간 넣고 있음
	                .setExpiration(Date.from(tokenValidity.toInstant())) // 토큰의 유효기간
	                .signWith(key, SignatureAlgorithm.HS256)  // 암호화
	                .compact();  // 토큰 생성됨
	        		// 토큰 생성되면 문자열로 반환됨
	    }
	
	
	// Claims을 파싱하는 메서드 - 받은 토큰에서 Claims 파싱
	public Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
	
	// Username(eamil)을 가지고 오기 위한 메서드
	public String getUserId(String token) {  
        return parseClaims(token).get("email", String.class);
    }
	
	// 받아온 토큰이 유효한 토큰인지 확인하는 메서드
	public boolean validateToken(String token, HttpServletRequest request) {
        Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
        return true;
   }
}
