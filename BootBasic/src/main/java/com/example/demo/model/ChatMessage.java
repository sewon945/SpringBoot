package com.example.demo.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString   // 콘솔에 출력되게끔 해주는 것
public class ChatMessage {
	
	// enum => 정해져 있는 값 중에 하나만 선택하고 싶을 때
	// WebSocketChatHandler에 handleTextMessage 함수에서..
	public enum MessageType{
		ENTER, QUIT, TALK
	}
	
	private MessageType messageType;  // ENTER, QUIT, TALK 중 하나만 선택 가능
	private String roomId;   // 채팅방 번호 구별
	private String sender;   // 채팅 보낸 사람이 누군지
	private String message;  // 채팅 내용
}
