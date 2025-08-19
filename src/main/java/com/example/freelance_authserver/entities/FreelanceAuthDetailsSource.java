package com.example.freelance_authserver.entities;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class FreelanceAuthDetailsSource implements AuthenticationDetailsSource<HttpServletRequest, FreelanceWebAuthenticationDetails> {
	@Override
	public FreelanceWebAuthenticationDetails buildDetails(HttpServletRequest request) {
		return new FreelanceWebAuthenticationDetails(request);
	}
}
