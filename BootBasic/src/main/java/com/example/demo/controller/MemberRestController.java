package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.BootMember;
import com.example.demo.service.MemberService;

@RestController
public class MemberRestController {   // 비동기 통신 ??
	
	// Service -> RestController
	@Autowired
	MemberService service;
	
	// 지금 get방식으로 member 요청하고 있음
	@GetMapping("/member")
	public List<BootMember> getAllMembers() {
		// Jackson 라이브러리 : 자바 Object => JSON Object
		// -> Boot에서는 기본으로 가지고 있음
		// 자동으로 JSON array 형식으로 바꿔줬다!!
		List<BootMember> list = service.getAllMembers();
		return list;
	}
	
	// MemberController 보면서 하기
	@PostMapping("/joinasync")
	public String joinAsync(@ModelAttribute BootMember member) {
//		System.out.println(member.getId());
//		System.out.println(member.getPw());
//		System.out.println(member.getNick());
		
		service.join(member);
		return "OK";   // 비동기 회원가입을 했을 때 F12 콘솔에서 ok가 뜨는지 확인
	}
	// 삭제 (순서 3)
	@DeleteMapping("/delete/{uid}")
	public String delete(@PathVariable("uid")Long uid) {
		service.delete(uid);
		
		return "OK";
	}
	
	// update 순서 4
	@PatchMapping("/update")
	public String update(@ModelAttribute BootMember member) {
		// 수정 (순서 6) - void -> String 바꿔주기(다른 것들도 다 바꿔줘야 함)
		service.update(member);
		return "OK";
	}
	
}
