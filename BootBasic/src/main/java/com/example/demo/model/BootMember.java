package com.example.demo.model;
// JPA를 사용하여 테이블 형태로 사용할 거임
// ORM : 자바 객체와 관계(오라클,mysql)를 매핑시켜주는 것?

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// window -> show view -> outline 에서 확인하면서 적기
@Getter
@Setter
@NoArgsConstructor  // 기본 생성자

// JPA (Java Persistence API) 사용
// : ORM(Object-Relational Mapping) 기술의 표준 (Java)
// JPA를 사용해서 ORM(자바 객체를 관계형 테이블과 매핑 시켜준 것)을 해주었다.
@Entity		// JPA Entity 추가
public class BootMember {
	// class에 적어준 BootMember가 클래스 이름이라고 생각하면 되고 컬럼이 id, pw, nick 이라고 생각하면 됨
	
	@Id		// Primary Key 로 지정
	@GeneratedValue(strategy=GenerationType.IDENTITY)	// IDENTITY : 1부터 자동으로 들어감
	private Long uid;  	// 삭제를 위해 인위적으로 long형 만들어줌
	// 먼저 필요한 세 가지 가져오기(index.jsp에서 쓴 세 가지 name을 똑같이 적어주기)
	private String id;
	private String pw;
	private String nick;
	
// mySQL에서 아무곳에 drop table boot_member; 라고 치고 restart하면 내가 만든 테이블이 콘솔창에 뜸
}
