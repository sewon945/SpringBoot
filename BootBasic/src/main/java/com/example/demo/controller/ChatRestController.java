package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.ChatRoom;
import com.example.demo.service.ChatService;

@RestController
public class ChatRestController {
	
	@Autowired
	ChatService service;
	
	// room.jsp의 경로 create에서 가져옴
	@PostMapping("create") 
	// room.jsp에서 콜론 앞에 쓴 키값 작성, value 가져오기로 했으니까 String타입
	public ChatRoom createRoom(@RequestParam("roomName")String roomName) {   
		// System.out.println(roomName);
		// 반환타입이 ChatRoom이며 ChatService 파일의 chatRoom에 대한 builer가 불러와짐
		return service.createRoom(roomName);   
	}
	
	@GetMapping("/rooms")   // 그냥 rooms으로 적든 /rooms로 적든 둘 다 호출됨
	public List<ChatRoom> findAllRoom() {
		return service.findAllRooms();
	}
}
