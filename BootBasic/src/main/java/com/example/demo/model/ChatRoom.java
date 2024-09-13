package com.example.demo.model;

import lombok.Builder;
import lombok.Getter;

// @AllArgsConstructor : lombok 사용해서 전체 필드 초기화하는 생성자 생성
@Getter
public class ChatRoom {
	
	private String roomId;    // 식별자
	private String roomName;  // 채팅방 이름

	// 생성자 호출하는 대신에 Builder 방식으로 객체 생성
	// ex) new ChatRoom("1", "채팅방1");  기존에 쓰던 방식 - 생성자 호출해서 객체 생성
	// => ChatRoom.builder().roomId("1").roomName("채팅방1")  ChatRoom에 대한 builder가 만들어짐
	// 장점 : 각 인자가 어떤 값을 의미하는지 바로 알아보기 좋아짐
	
	@Builder
	// ChatRoom: 생성자, 매개변자 2개 넘겨주기
	public ChatRoom(String roomId, String roomName) {
		// 넘겨받은 거 초기화 해주기
		this.roomId = roomId;
		this.roomName = roomName;
	}
}
