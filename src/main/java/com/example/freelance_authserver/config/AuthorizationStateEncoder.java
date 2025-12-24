package com.example.freelance_authserver.config;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import com.example.freelance_authserver.entities.AuthorizationState;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AuthorizationStateEncoder {

	private static final ObjectMapper mapper = new ObjectMapper();

	public static String encode(AuthorizationState state) {
		try {
			String json = mapper.writeValueAsString(state);
			return Base64.getUrlEncoder().withoutPadding().encodeToString(json.getBytes(StandardCharsets.UTF_8));
		} catch (Exception e) {
			throw new IllegalStateException("Failed to encode AuthorizationState", e);
		}
	}
}
