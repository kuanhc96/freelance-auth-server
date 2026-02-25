package com.example.freelance_authserver.config;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.freelance_authserver.entities.UserCredentialsEntity;
import com.example.freelance_authserver.repository.UserCredentialsRepository;
import lombok.RequiredArgsConstructor;

import com.example.freelance_authserver.entities.FreelanceWebAuthenticationDetails;
import com.example.freelance_authserver.enums.UserRole;

@Component
@RequiredArgsConstructor
public class EmailPasswordRoleAuthenticationProvider implements AuthenticationProvider {
	private final UserCredentialsRepository userCredentialsRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String email = authentication.getName();
		String password = authentication.getCredentials().toString();
		FreelanceWebAuthenticationDetails details = (FreelanceWebAuthenticationDetails) authentication.getDetails();
		UserRole role = details.getRole();
		String userGUID = authenticate(email, role, password);
		if (StringUtils.isNotBlank(userGUID)) {
			UsernamePasswordAuthenticationToken principal = new UsernamePasswordAuthenticationToken(email, password, List.of(new SimpleGrantedAuthority(role.name())));
			principal.setDetails(Map.of("userGUID", userGUID));
			return principal;
		} else {
			throw new BadCredentialsException("Invalid password!");
		}
	}

	public String authenticate(String email, UserRole role, String password) {
		Optional<UserCredentialsEntity> optionalUserEntity = userCredentialsRepository.getUserByEmailAndRole(email, role);
		UserCredentialsEntity userCredentialsEntity = optionalUserEntity
				.orElseThrow(() -> new BadCredentialsException("User Not Found for email: " + email));
		boolean isMatched = passwordEncoder.matches(password, userCredentialsEntity.getPassword());
		if (isMatched) {
			return userCredentialsEntity.getUserGUID();
		} else {
			return null;
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
	}
}
