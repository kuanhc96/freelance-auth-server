package com.example.freelance_authserver.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import com.example.freelance_authserver.entities.CreateUserRequest;
import com.example.freelance_authserver.entities.CreateUserResponse;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
//	private final UserManagementServerClient userManagementServerClient;
//
//	@PostMapping
////	@PreAuthorize("INTEGRATION_TEST")
//	public ResponseEntity<CreateUserResponse> createUser(@RequestBody CreateUserRequest request) {
//		return userManagementServerClient.createUser(request);
//	}
//
//	@DeleteMapping("/{userGUID}")
////	@PreAuthorize("INTEGRATION_TEST")
//	public ResponseEntity<Void> deleteUser(@PathVariable String userGUID) {
//		userManagementServerClient.deleteUser(userGUID);
//		return ResponseEntity.noContent().build();
//	}
}
