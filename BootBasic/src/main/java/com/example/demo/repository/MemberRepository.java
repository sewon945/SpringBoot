package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.BootMember;

// 레파지토리를 쓸 때에는 인터페이스로 파일 설정해주기
// JPA를 사용하기 위해서는 특정 객체를 다룰 Repository 인터페이스 필수로 만들기!
// extends JpaRepository<객체타입 , 기본키의 타입>
public interface MemberRepository extends JpaRepository<BootMember, Long> {
	
	// login 순서3 - Service 한 번 보고 오기
	// select * from boot_member where id=? and pw=?   만들고 싶음
	// 추상메서드로 작성해주면 됨
	// 위의 sql문이 호출될 수 있게 만들려면?
	public BootMember findByIdAndPw(String id, String pw);   // By : where절
	
}
