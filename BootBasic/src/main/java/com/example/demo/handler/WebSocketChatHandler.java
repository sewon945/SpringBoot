package com.example.demo.handler;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.example.demo.model.ChatMessage;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

// 소켓 통신 : 1(서버) : N(클라이언트) 관계
// 한 서버에서 여러 클라이언트가 발송한 메시지를 받아 처리해주는 역할 -> Handler(접속, 접속해제, 메시지처리)는 기본적으로 정의해줘야함
@Component   // 따로 객체(bean)를 등록할 필요 없이 객체(bean) 생성 어노테이션
@RequiredArgsConstructor    // 초기화가 되지 않은 필드 초기화하는 생성자 만들어 줌 -> objectMapper 객체 생성(@Autowired와 비슷함)
// Spring 5버전 이상에 버전을 사용 시에는 @RequiredArgsConstructor를 쓰는 것을 권장함
// Autowired를 꺼리는 이유 => 순환 참조 방지 / final이라는 상수는 무조건 초깃값을 줘야함(초깃값 안주면 오류)
// @RequiredArgsConstructor는 final 키워드 지원해줌 => 값이 마음대로 변경되지 않도록 막을 수 있음
public class WebSocketChatHandler extends TextWebSocketHandler {

	// sessions : 현재까지 접속한 모든 클라이언트 정보 저장 
	// - Set 사용(순서없음)
	private Set<WebSocketSession> sessions =  new HashSet<>();
 
	
	// chatRoomSessions : 각 채팅 룸별(key) 클라이언트 정보 따로 저장 
	// - Map 방 별로 구별해야하기에 key, value 필요(순서필요)
	// String : 룸 id
	private Map<String, Set<WebSocketSession>> chatRoomSessions = new HashMap<>();
	
	
	// 객체 생성하기
	private final ObjectMapper objectMapper;

	
	
	
	// afterConnectionEstablished : 클라이언트 접속 시 호출 - sessions에 접속한 클라이언트 추가
	// afterConnectionEstablished 자동 완성
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		sessions.add(session);   // 매개인자로 넘어오는 session 그대로 추가(WebSocketSession session)
		System.out.println("접속 : " + session.getId());
	}

	
	// afterConnectionClosed : 클라이언트 접속 해제 시 호출 - sessions에 접속 해제하는 클라이언트 제거
	// afterConnectionClosed 자동 완성
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		sessions.remove(session);  // 매개인자를 그대로 삭제해주면 됨
		System.out.println("해제 : " + session.getId());
	}
	
	
	// 메시지 처리 하기
	// handleTextMessage 자동완성
	@Override               // (채팅을 보낸 클라이언트(누구인지)의 정보, 그 클라이언트가 보낸 메세지(어떤메세지인지))가 들어옴
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		// 클라이언트가 보낸 메세지 확인하기 (TextMessage 타입만 보면 알아보기 어려우니 ChatMessage(VO)로 따로 만들기(정의하기))
		// 메세지의 기능 확인
		String payload = message.getPayload();   // 클라이언트가 보낸 메세지 가져올 수 있는 기능 - getPayload() 반환타입(String)
		// ObjectMapper 사용해서 String타입의 반환타입을 Java Class(ChatMessage) 형태로 변환
		ChatMessage chatMessage = objectMapper.readValue(payload, ChatMessage.class); // (뭘 바꿀건지, 어떤 형식을 바꿀건지)
		// ChatMessage(messageType=ENTER
		System.out.println(chatMessage.toString());
		// 룸 아이디 별로
		String roomId = chatMessage.getRoomId();
		
		// 메세지 -> 채팅방에 대한 내용이 포함 (roomId 필요)
		// 만약 해당 채팅방이 MAP에 등록되어 있지 않으면 추가 해주는 작업 수행
		
		// roomId가 맵의 키로 등록되어 있지 않을 경우(key, value)
		if(!chatRoomSessions.containsKey(roomId)) {
			chatRoomSessions.put(roomId, new HashSet<>());
		}
		
		// roomId로 해당 채팅방에 접속된 클라이언트 정보 저장해놓은 set 가져오기
		// session이 추가가 됐으면 해당 방에 있는 클라이언트들 가져오기
		Set<WebSocketSession> chatRoomSession = chatRoomSessions.get(roomId);
		
		// 메세지 -> 클라이언트가 접속(ENTER) or 해제(QUIT) or 메세지(TALK)
		// 접속 메세지 경우 - 해당 클라이언트를 채팅중인 채팅방 MAP에 추가하기	
		// chatMessage에서 MessageType이 chatMessage.getMessageType().ENTER와 같은지
		if(chatMessage.getMessageType().equals(chatMessage.getMessageType().ENTER)) {
			chatRoomSession.add(session);  //WebSocketSession의 session
			
		// 채팅방 나가기
		} else if(chatMessage.getMessageType().equals(chatMessage.getMessageType().QUIT)) { 
			chatRoomSession.remove(session); // 현재 세션 삭제하기(나간다고 했을 때 빼내주기)
		}
		
		// 해제 메시지 경우 - 나가는 경우 채팅방 MAP에서 제거
		// 메세지(채팅) 경우 - 해당 채팅 내용은 해당 채팅방에 들어와있는 모든 클라이언트에게 공유하기
		// parallelStream() - 통로를 만들어서 set 안에 있는 컬럼들을 하나하나 뽑기, cSession - 클라이언트세션
		chatRoomSession.parallelStream().forEach( cSession -> {
			// 특정 클라이언트가 서버한테 보낸 메세지를 해당 채팅방에 들어와 있는 모든 클라이언트에게 메세지로 전송하는 것
			try {  // try catch로 예외처리 해주기
				cSession.sendMessage(message);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
		});
	}
}
