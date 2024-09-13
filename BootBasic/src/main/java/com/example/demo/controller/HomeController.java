package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class HomeController {
	// localhost:8090/myapp
	// @RequestMapping("/", rRe)

	// [GET]localhost:8090/myapp/
	@GetMapping(value = "/")
	public String indexPage() {
		return "index";
	}

	// room.jsp 
	@GetMapping("/room")
	public String roomPage() {
		return "room";
	}
	
	// chat.jsp 리턴하는 부분(채팅방 구별X)
	@GetMapping("/chat/{roomId}")
	public String chatPage(@PathVariable("roomId") String roomId, Model model) {
		// roomId를 model에 저장하고 있는 상태에서 이동
		model.addAttribute("roomId", roomId);
		return "chat";
	}
}
