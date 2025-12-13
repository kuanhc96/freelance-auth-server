package com.example.freelance_authserver.config;

import com.example.freelance_authserver.entities.AuthorizationState;

public class AuthorizationStateCodec {
	public static String encode(AuthorizationState state) {
		String payload = AuthorizationStateEncoder.encode(state);
		String signature = AuthorizationStateSigner.sign(payload);
		return payload + "." + signature;
	}
}
