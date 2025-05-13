package com.LetsEat.StudyOn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

// 인증 미구현으로 security 기능 꺼둠
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class StudyOnApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudyOnApplication.class, args);
	}

}
