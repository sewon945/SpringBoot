package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// ** @SpringBootApplication : 설정 자동화 어노테이션 **
// => 해당 어노테이션이 있는 위치로부터 설정 내용을 읽기 때문에 이 어노테이션을 포함한 클래스는 프로젝트의 최상단에 위치해야 함
// com -> example -> demo -> App.java -> @SpringBootApplication
// => com.example.demo 아래쪽에 어플리케이션을 만들어야 함(서비스 파일 등)

@SpringBootApplication
public class BootBasicApplication {

	public static void main(String[] args) {
		SpringApplication.run(BootBasicApplication.class, args);
	}

}
