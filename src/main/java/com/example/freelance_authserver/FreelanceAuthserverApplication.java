package com.example.freelance_authserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class FreelanceAuthserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(FreelanceAuthserverApplication.class, args);
	}

}
