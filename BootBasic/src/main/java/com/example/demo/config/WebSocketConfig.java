package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.example.demo.handler.WebSocketChatHandler;

import lombok.RequiredArgsConstructor;

@Configuration  // 설정 파일
@EnableWebSocket  // 웹소켓 활성화
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {  // class 만들 때 interface에서 WebSocketConfigurer 추가
	
	private final WebSocketChatHandler webSocketChatHandler;
	
	//registerWebSocketHandlers : 여기서 무조건 정의해줘야 함 - 정의했으면 등록도 해줘야 함
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {  // registe : 등록하다
		// registry 활용해서 등록하기
		// EndPoint : 서비스의 끝자락(핸드폰/PC) 의미 / WebSocket에서는 URI(경로)의 끝자락 의미
		//     => 채팅을 하기 위해서 접근 해야하는 URL 의미 / localhost:8090/myapp/ws/chat
		// setAllowedOrigins : CORS 설정 - boot랑 react 연결할 때 쓰는 것처럼 서로 다른 자원에서 접근하는 것(접근 권한 부여)
		//  setAllowedOrigins("*")는 옵션임
		// 코스 설정해서 등록해주기
		registry.addHandler(webSocketChatHandler, "/ws/chat").setAllowedOrigins("*");
	}

}
