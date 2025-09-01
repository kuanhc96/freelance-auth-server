package com.example.freelance_authserver.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.example.freelance_authserver.entities.CreateUserRequest;
import com.example.freelance_authserver.entities.CreateUserResponse;
import com.example.freelance_authserver.entities.FreelanceAppUserDetails;
import com.example.freelance_authserver.entities.UserEntity;
import com.example.freelance_authserver.enums.UserRole;
import com.example.freelance_authserver.enums.UserStatus;
import com.example.freelance_authserver.repository.UserRepository;
import com.example.freelance_authserver.translator.UserTranslator;

@Service
@RequiredArgsConstructor
public class UserDetailsService {
	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;

	public CreateUserResponse createUser(CreateUserRequest request) {
		UserEntity userEntity = UserTranslator.toEntity(request);
		String newStudentGUID = UUID.randomUUID().toString();
		LocalDateTime now = LocalDateTime.now();
		userEntity.setUserGUID(newStudentGUID);
		userEntity.setPassword(passwordEncoder.encode(newStudentGUID));
		userEntity.setCreatedDate(now);
		userEntity.setUpdatedDate(now);
		userEntity.setStatus(UserStatus.CREATED);
		userRepository.insertUser(userEntity);
		return UserTranslator.toDto(userEntity);
	}

	public UserDetails loadUserByEmailAndRole(String email, UserRole role) {
		Optional<UserEntity> optionalUserEntity = userRepository.getUserByEmailAndRole(email, role);
		UserEntity userEntity = optionalUserEntity
				.orElseThrow(() -> new BadCredentialsException("User Not Found for email: " + email));
		List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + userEntity.getRole().name()));
		return new FreelanceAppUserDetails(
				userEntity.getUserGUID(), userEntity.getEmail(), userEntity.getRole(), userEntity.getPassword(), authorities
		);
	}

	public UserDetails loadUserByUsername(String email) {
		return loadUserByEmailAndRole(email, UserRole.STUDENT);
	}
}
