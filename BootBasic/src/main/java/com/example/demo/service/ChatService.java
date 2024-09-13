package com.example.demo.service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.demo.model.ChatRoom;

@Service
public class ChatService {
	// room id, 객체 넣어주기
	// 생성된 채팅 룸 정보 저장할 맵 <key(임의로 생성할 roomId-value>
	private Map<String, ChatRoom> chatRooms = new HashMap<>();   //chatRooms - 전역변수, DB에서 호출?
	
	// 모든 채팅방 불러오기
	public List<ChatRoom> findAllRooms() {
		// value들만 뽑아서 리스트 안에 넣어주기
		return new ArrayList<>(chatRooms.values());
		
	}
	
	// 새 채팅방 생성할 때 만드는 메서드
	public ChatRoom createRoom(String roomName) {
		String roomId = UUID.randomUUID().toString();   // 유일한 랜덤 뮨자열 생성
		ChatRoom room = ChatRoom.builder()
						.roomId(roomId)
						.roomName(roomName)
						.build();   // id와 name을 가지고 있는 build를 chatroom에 넣어주기?
		chatRooms.put(roomId,room);  // 키값과 value값 넣어줘서 chatRooms에 차곡차곡 저장하기
		return room;
	}
}
