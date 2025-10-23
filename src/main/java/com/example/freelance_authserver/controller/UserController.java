package com.example.freelance_authserver.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;

import com.example.freelance_authserver.entities.CreateUserRequest;
import com.example.freelance_authserver.entities.CreateUserResponse;
import com.example.freelance_authserver.service.UserDetailsService;

@RestController
@RequiredArgsConstructor
public class UserController {
	private final UserDetailsService userDetailsService;

	@PostMapping("/user/create")
	public ResponseEntity<CreateUserResponse> createUser(@RequestBody CreateUserRequest request) {
		if (!request.getEmail().contains("@")) {
			throw new IllegalArgumentException("Invalid email format");
		}
		if (StringUtils.isBlank(request.getPassword())) {
			throw new IllegalArgumentException("Password cannot be empty");
		}
		if (StringUtils.isBlank(request.getName())) {
			throw new IllegalArgumentException("Name cannot be empty");
		}

		return ResponseEntity.status(HttpStatus.CREATED).body(userDetailsService.createUser(request));
	}

	@DeleteMapping("/user/delete/{userGUID}")
	public ResponseEntity<Void> deleteUser(@PathVariable String userGUID) {
		if (StringUtils.isBlank(userGUID)) {
			throw new IllegalArgumentException("User GUID cannot be null or empty");
		}
		userDetailsService.deleteUser(userGUID);
		return ResponseEntity.noContent().build();
	}
}
