package com.example.freelance_authserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;

@Configuration
public class AuthServerClientConfig {
	@Bean
	public Encoder feignFormEncoder() {
		return new SpringFormEncoder();
	}
}
