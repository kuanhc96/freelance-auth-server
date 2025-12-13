package com.example.freelance_authserver.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.freelance_authserver.config.AuthorizationStateDecoder;

@RestController
@RequestMapping("/authState")
public class AuthorizationStateController {

	@PostMapping("/verify")
	public ResponseEntity<Boolean> verifyAuthorizationState(@RequestBody String state) {
		return ResponseEntity.ok(AuthorizationStateDecoder.verifySignature(state));
	}

}
