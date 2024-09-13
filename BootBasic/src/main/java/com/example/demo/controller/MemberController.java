package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.model.BootMember;
import com.example.demo.service.MemberService;

import jakarta.servlet.http.HttpSession;

@Controller	  // 일반적으로 View(.jsp)를 반환해줌 => 화면 자체가 새롭게 응답이 된다(새화면이 된다)
// 비동기 통신 => 전체화면이 바뀌는 게 아니라 일부만 바뀌는 통신 방법(뷰 전체를 응답받는 게 아니라 일부 데이터만 응답받는 통신)
// 			   @Controller + @ResponseBody
//			   => @RestController : data(model) 반환이 기본!(모델만 반환할 때?)
public class MemberController {
	
	@Autowired
	MemberService service;
	
	// mapper
	@PostMapping("/join")
	// 안 될 경우 새로고침 해야하고 그래도 안 될 경우 경로 다시 확인 해주기
	public String join(@ModelAttribute BootMember member) {
		// 브라우저 http://localhost:8090/myapp/ 에서 회원가입을 하고 버튼을 누르면 
		//  http://localhost:8090/myapp/join 이렇게 url이 바뀌면서 콘솔에 내가 친 id, pw, nick이 뜬다
//		System.out.println(member.getId());
//		System.out.println(member.getPw());
//		System.out.println(member.getNick());
		
		// MyBatis : controller(요청/응답(view or data)) -> service -> mapper(Interface, xml)
		// JPA     : controller -> service -> repository(Interface -> sql 작성X, 정의가 된 메서드 호출해서 사용)
		
		// 레파지토리 파일 생성 후 생성
		service.join(member);   // 넘어오는 값 그대로 넘겨주기
		
		// redirect:/ 쓰면 http://localhost:8090/myapp/ 이 뒤에 경로가 붙지 않음
		return "redirect:/";   // index.js 를 다시 리턴해주게끔?
		
		// mySQL에서 select * from boot_member; 적고 테이블 생성됐는 지 확인
	}
	
	
	// login 순서 2, 5
	@PostMapping("/login")
	public String login(@ModelAttribute BootMember member, HttpSession session) {
		// login 순서 5
		BootMember result = service.login(member);
		session.setAttribute("member", result);
		return "redirect:/";
	}

}
