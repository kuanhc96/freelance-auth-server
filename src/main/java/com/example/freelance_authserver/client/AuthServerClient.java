package com.example.freelance_authserver.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.freelance_authserver.config.AuthServerClientConfig;
import com.example.freelance_authserver.entities.GetAccessTokenRequest;

@FeignClient(name = "freelance-authserver", configuration = AuthServerClientConfig.class)
public interface AuthServerClient {
	@PostMapping(value = "/oauth2/token", consumes = "application/x-www-form-urlencoded")
	TokenResponse getToken(GetAccessTokenRequest tokenRequest);

	class TokenResponse {
		public String access_token;
		public String token_type;
		public int expires_in;
		public String scope;
	}
}
