package com.example.freelance_authserver.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import com.example.freelance_authserver.entities.CreateUserRequest;
import com.example.freelance_authserver.entities.CreateUserResponse;
import com.example.freelance_authserver.service.UserDetailsService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LoginController {
	private final UserDetailsService userDetailsService;

	@PostMapping("/createUser")
	public ResponseEntity<CreateUserResponse> createUser(@RequestBody CreateUserRequest request) {
		return ResponseEntity.ok(userDetailsService.createUser(request));
	}
}
