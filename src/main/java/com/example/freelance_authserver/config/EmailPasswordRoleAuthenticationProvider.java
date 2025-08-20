package com.example.freelance_authserver.config;

import java.util.List;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import com.example.freelance_authserver.entities.FreelanceAppUserDetails;
import com.example.freelance_authserver.entities.FreelanceWebAuthenticationDetails;
import com.example.freelance_authserver.enums.UserRole;
import com.example.freelance_authserver.service.UserDetailsService;

@Component
@RequiredArgsConstructor
public class EmailPasswordRoleAuthenticationProvider implements AuthenticationProvider {
	private final UserDetailsService userDetailsService;
	private final PasswordEncoder passwordEncoder;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String email = authentication.getName();
		String password = authentication.getCredentials().toString();
		FreelanceWebAuthenticationDetails details = (FreelanceWebAuthenticationDetails) authentication.getDetails();
		UserRole role = details.getRole();
		FreelanceAppUserDetails userDetails = (FreelanceAppUserDetails) userDetailsService.loadUserByEmailAndRole(email, role);
		if (passwordEncoder.matches(password, userDetails.getPassword())) {
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
