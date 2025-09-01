package com.example.freelance_authserver.entities;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

import jakarta.servlet.http.HttpServletRequest;

import com.example.freelance_authserver.enums.UserRole;

public class FreelanceWebAuthenticationDetails extends WebAuthenticationDetails {
	private final UserRole role;

	public FreelanceWebAuthenticationDetails(HttpServletRequest request) {
		super(request);
		this.role = UserRole.valueOf(request.getParameter("role"));
	}

	public UserRole getRole() {
		return role;
	}
}
