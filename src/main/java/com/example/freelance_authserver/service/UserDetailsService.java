package com.example.freelance_authserver.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.example.freelance_authserver.entities.FreelanceAppUserDetails;
import com.example.freelance_authserver.entities.UserEntity;
import com.example.freelance_authserver.enums.UserRole;
import com.example.freelance_authserver.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserDetailsService {
	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;

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
