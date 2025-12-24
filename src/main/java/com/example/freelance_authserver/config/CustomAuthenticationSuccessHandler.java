package com.example.freelance_authserver.config;

import java.io.IOException;
import java.net.URI;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.example.freelance_authserver.entities.AuthorizationState;

public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
		// Custom logic can be added here if needed
		response.addHeader("Access-Control-Allow-Origin", "http://localhost:8080");
		String state;
		if (StringUtils.isNotBlank(request.getParameter("rememberMe"))) {
			AuthorizationState authState = AuthorizationState.builder()
					.rememberMe(true)
					.successUrl("http://localhost:8080/dashboard")
					.build();
			state = AuthorizationStateCodec.encode(authState);
		} else {
			AuthorizationState authState = AuthorizationState.builder()
					.rememberMe(false)
					.successUrl("http://localhost:8080/dashboard")
					.build();
			state = AuthorizationStateCodec.encode(authState);
		}
		URI uri = UriComponentsBuilder.fromUriString("/oauth2/authorize")
				.queryParam("client_id", "fe-client")
				.queryParam("redirect_uri", "http://localhost:8080/callback")
				.queryParam("response_type", "code")
				.queryParam("scope", "openid")
				.queryParam("state", state)
				.build().toUri();


		response.sendRedirect(uri.toString());

		// Call the parent class's method to handle the redirect
		super.onAuthenticationSuccess(request, response, authentication);
	}
}
