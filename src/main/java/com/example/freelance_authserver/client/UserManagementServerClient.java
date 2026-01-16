package com.example.freelance_authserver.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.freelance_authserver.config.OAuth2FeignConfig;
import com.example.freelance_authserver.entities.CreateUserRequest;
import com.example.freelance_authserver.entities.CreateUserResponse;
import com.example.freelance_authserver.entities.GetUserResponse;
import com.example.freelance_authserver.enums.UserRole;

@FeignClient(name = "freelance-user-management-server", configuration = OAuth2FeignConfig.class)
public interface UserManagementServerClient {
	@PostMapping(value = "/api/users/create", consumes = "application/json")
	ResponseEntity<CreateUserResponse> createUser(@RequestBody CreateUserRequest request);

	@DeleteMapping(value = "/api/users/delete/{userGUID}")
	ResponseEntity<Void> deleteUser(@PathVariable String userGUID);

	@GetMapping(value = "/api/users")
	ResponseEntity<GetUserResponse> getUserByEmailAndRole(@RequestParam String email, @RequestParam UserRole role);

	@PostMapping(value = "/api/users/authenticate", consumes = "application/x-www-form-urlencoded")
	ResponseEntity<String> authenticate(@RequestParam String email, @RequestParam UserRole role, @RequestParam String password);
}
