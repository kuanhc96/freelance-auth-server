package com.example.freelance_authserver.config;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
		// Custom logic can be added here if needed
		response.addHeader("Access-Control-Allow-Origin", "http://localhost:8080");

		// Call the parent class's method to handle the redirect
		super.onAuthenticationSuccess(request, response, authentication);
	}
}
