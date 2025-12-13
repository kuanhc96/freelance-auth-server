package com.example.freelance_authserver.entities;

import lombok.Builder;

@Builder
public record AuthorizationState(boolean rememberMe, String successUrl) {}
