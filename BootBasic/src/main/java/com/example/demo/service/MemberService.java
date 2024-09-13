package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.BootMember;
import com.example.demo.repository.MemberRepository;

@Service
public class MemberService {
	// 레파지토리 파일 생성하고 나서 생성
	// Respository -> Service
	@Autowired
	MemberRepository repo;
	
	// 회원가입 할 때 쓰는 레파지토리
	public void join(BootMember member) {
		// insert into boot_member values (id, pw, nick) => member(=id,pw,nick) 객체가 가지고 있음
		repo.save(member);   // 넣어주고 싶은 값 DB에 넣어줘서 저장
		
	}
	
	// '전체 회원 정보'를 불러 올 수 있는 기능 만들기
	public List<BootMember> getAllMembers() {
		List<BootMember> list = repo.findAll();  // select * from boot_member : 전체 회원 정보 불러오기
		return list;
	}
		
		
		// login 순서 4
		public BootMember login(BootMember member) {
			return repo.findByIdAndPw(member.getId(), member.getPw());  // 따로따로 빼서 넣어줘야함
	}
		
		// 삭제 (순서 4)
		public void delete(Long uid) {   // Long형의 uid 보내주기
			repo.deleteById(uid);   // By : where절 , Id : 프라이머리 키
		}
		
		// 수정 (순서 5)
		public void update(BootMember member) {
			// JPA 활용 update 하기
			
			// 1. 수정하고 싶은 행 가져오기(read) -> 즉, select 하기 - 특정한 회원 정보 가져오기
			// Optional<> : NPE 방지 (넘기는 기본키 값이 잘못된 경우)
			Optional<BootMember> find = repo.findById(member.getUid());
			
			// 2. setter 메서드 사용해서 find 객체의 필드값 수정하기
			find.get().setPw(member.getPw());  // null일 수도 있기에 먼저 get 함수 써주기
			find.get().setNick(member.getNick());
			
			// 3. 수정된 값을 가진 find 객체의 값을 DB에 저장하기
			//  * save : 무조건 insert를 호출하는 것은 아님
			//  => uid를 사용해서 기존에 존재하는 uid를 가진 객체를 저장하는 경우에는 insert가 아니라 update를 해줌
			// 기존에 존재하지 않는 uid를 가진 객체를 저장하는 경우에는 insert 해줌
			repo.save(find.get());  // get 호출해서 BootMember 넣어주기(get 호출하면 부트멤버는 불러짐)
			
		}
}
