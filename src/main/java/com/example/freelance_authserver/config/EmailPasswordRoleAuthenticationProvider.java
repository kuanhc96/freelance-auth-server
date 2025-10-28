package com.example.freelance_authserver.config;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import com.example.freelance_authserver.client.UserManagementServerClient;
import com.example.freelance_authserver.entities.FreelanceWebAuthenticationDetails;
import com.example.freelance_authserver.enums.UserRole;

@Component
@RequiredArgsConstructor
public class EmailPasswordRoleAuthenticationProvider implements AuthenticationProvider {
	private final UserManagementServerClient userManagementServerClient;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String email = authentication.getName();
		String password = authentication.getCredentials().toString();
		FreelanceWebAuthenticationDetails details = (FreelanceWebAuthenticationDetails) authentication.getDetails();
		UserRole role = details.getRole();
		ResponseEntity<Boolean> authenticationResponse = userManagementServerClient.authenticate(email, role, password);
		if (authenticationResponse.getBody() != null && authenticationResponse.getBody()) {
			return new UsernamePasswordAuthenticationToken(email, password, List.of(new SimpleGrantedAuthority(role.name())));
		} else {
			throw new BadCredentialsException("Invalid password!");
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
	}
}
