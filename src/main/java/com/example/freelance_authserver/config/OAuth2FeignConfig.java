package com.example.freelance_authserver.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.RequestInterceptor;

import com.example.freelance_authserver.client.AuthServerClient;
import com.example.freelance_authserver.entities.GetAccessTokenRequest;

@Configuration
public class OAuth2FeignConfig {
	@Value("${spring.security.oauth2.client.registration.freelance-authserver.client-id}") String clientId;
	@Value("${spring.security.oauth2.client.registration.freelance-authserver.client-secret}") String clientSecret;
	@Value("${spring.security.oauth2.client.registration.freelance-authserver.scope}") String scope;

	@Bean
	public RequestInterceptor oauth2FeignRequestInterceptor(AuthServerClient authServerClient)  {
		return template -> {
			GetAccessTokenRequest getAccessTokenRequest = GetAccessTokenRequest.builder()
					.grant_type("client_credentials")
					.client_id(clientId)
					.client_secret(clientSecret)
					.scope(scope)
					.build();
			AuthServerClient.TokenResponse tokenResponse = authServerClient.getToken(getAccessTokenRequest);
			template.header("Authorization", "Bearer " + tokenResponse.access_token);
		};
	}

}
