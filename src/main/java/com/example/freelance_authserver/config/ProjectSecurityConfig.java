package com.example.freelance_authserver.config;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import jakarta.servlet.http.HttpServletRequest;

import com.example.freelance_authserver.client.UserManagementServerClient;
import com.example.freelance_authserver.entities.FreelanceAuthDetailsSource;
import com.example.freelance_authserver.enums.UserRole;
import com.example.freelance_authserver.filter.CsrfCookieFilter;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

@Configuration
@EnableWebSecurity
public class ProjectSecurityConfig {
	@Bean
	@Order(1)
	public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http)
			throws Exception {
		OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
				OAuth2AuthorizationServerConfigurer.authorizationServer();

		http
				.securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())
				.with(authorizationServerConfigurer, (authorizationServer) ->
						authorizationServer
								.oidc(Customizer.withDefaults())	// Enable OpenID Connect 1.0
				)
				.authorizeHttpRequests((authorize) ->
						authorize
								.anyRequest().authenticated()
				)
				// Redirect to the login page when not authenticated from the
				// authorization endpoint
				.exceptionHandling((exceptions) -> exceptions
						.defaultAuthenticationEntryPointFor(
								new LoginUrlAuthenticationEntryPoint("/login"),
								new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
						)
				);

		return http.build();
	}

	@Bean
	@Order(2)
	public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http, FreelanceAuthDetailsSource detailsSource)
			throws Exception {
		CsrfTokenRequestAttributeHandler csrfTokenRequestAttributeHandler = new CsrfTokenRequestAttributeHandler();
		http
				.authorizeHttpRequests((authorize) ->
						authorize
								.requestMatchers(
										"/",
										"/index.html",
										"/assets/**",
										"/*.js",
										"/*.css",
										"/.ico",
										"/*.png",
										"/*.svg",
										"/login",
										"/css/**",
										"/js/**",
										"/images/**",
										"/actuator/**",
										"/user/create",
										"/user/delete/**",
										"/oauth2/token"

								).permitAll()
								.requestMatchers("/api/**").authenticated()
								.anyRequest().permitAll()
				)
				.cors(corsConfig ->
						corsConfig.configurationSource(new CorsConfigurationSource() {
					@Override
					public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
						CorsConfiguration config = new CorsConfiguration();
						config.setAllowedOrigins(Collections.singletonList("http://localhost:8080"));
						config.setAllowedMethods(Collections.singletonList("*"));
						config.setAllowCredentials(true);
						config.setAllowedHeaders(Collections.singletonList("*"));
						config.setMaxAge(3600L);
						return config;
					}
				})
				)
				.csrf(csrf -> csrf
						.csrfTokenRequestHandler(csrfTokenRequestAttributeHandler)
						.ignoringRequestMatchers(
								"/actuator/**",
										"/user/create",
										"/user/delete/**"
						)
						.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
				)
				.formLogin(flc -> flc
						.loginPage("/login")
//						.defaultSuccessUrl("/")
								.authenticationDetailsSource(detailsSource)
//						.permitAll()
				);
		http.addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public RegisteredClientRepository registeredClientRepository() {
		RegisteredClient itClient = RegisteredClient.withId(UUID.randomUUID().toString())
				.clientId("freelance-integration-test-client")
				.clientSecret("{noop}secret")
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
				.authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
//				.scopes(scopeConfig -> scopeConfig.addAll(List.of("INTEGRATION_TEST")))
				.scope("INTEGRATION_TEST")
				.tokenSettings(TokenSettings.builder()
						.accessTokenTimeToLive(java.time.Duration.ofMinutes(10))
						.accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
						.build())
				.build();

		RegisteredClient resourceServerClient = RegisteredClient.withId(UUID.randomUUID().toString())
				.clientId("resource-server-client")
				.clientSecret("{noop}resourceServerSecret")
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
				.authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
//				.scopes(scopeConfig -> scopeConfig.addAll(List.of("INTEGRATION_TEST")))
				.scope("USER_MANAGEMENT_SERVER")
				.tokenSettings(TokenSettings.builder()
						.accessTokenTimeToLive(java.time.Duration.ofMinutes(10))
						.accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
						.build())
				.build();

		RegisteredClient authServerClient = RegisteredClient.withId(UUID.randomUUID().toString())
				.clientId("auth-server-client")
				.clientSecret("{noop}authServerSecret")
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
				.authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
//				.scopes(scopeConfig -> scopeConfig.addAll(List.of("INTEGRATION_TEST")))
				.scope("USER_MANAGEMENT_SERVER")
				.tokenSettings(TokenSettings.builder()
						.accessTokenTimeToLive(java.time.Duration.ofMinutes(10))
						.accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
						.build())
				.build();

		RegisteredClient feClient = RegisteredClient.withId(UUID.randomUUID().toString())
				.clientId("fe-client")
				.clientSecret("{noop}secret1")
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
				.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
				.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
				.redirectUri("https://app.insomnia.rest/oauth/redirect")
				.scopes(scopeConfig -> scopeConfig.addAll(List.of(OidcScopes.OPENID, "FREELANCE_FE")))
				.tokenSettings(TokenSettings.builder()
						.accessTokenTimeToLive(Duration.ofMinutes(10))
						.refreshTokenTimeToLive(Duration.ofHours(8))
						.accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
						.reuseRefreshTokens(false)
						.build())
				.build();

		RegisteredClient pkceFeClient = RegisteredClient.withId(UUID.randomUUID().toString())
				.clientId("fe-public-client")
				.clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
				.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
				.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
				.redirectUri("http://localhost:8080/callback")
				.scopes(scopeConfig -> scopeConfig.addAll(List.of(OidcScopes.OPENID, "FREELANCE_FE")))
				.clientSettings(ClientSettings.builder()
						.requireProofKey(true)
						.build())
				.tokenSettings(TokenSettings.builder()
						.accessTokenTimeToLive(Duration.ofMinutes(10))
						.refreshTokenTimeToLive(Duration.ofHours(8))
						.accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
						.reuseRefreshTokens(false)
						.build())
				.build();
		RegisteredClient gatewayServerClient = RegisteredClient.withId(UUID.randomUUID().toString())
				.clientId("gateway-server-client")
				.clientSecret("{noop}gatewayServerSecret")
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
				.authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
				.scopes(scopeConfig -> scopeConfig.addAll(List.of("USER_MANAGEMENT_SERVER", "RESOURCE_SERVER", "AUTHENTICATION_SERVER")))
				.tokenSettings(TokenSettings.builder()
						.accessTokenTimeToLive(java.time.Duration.ofMinutes(10))
						.accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
						.build())
				.build();

		return new InMemoryRegisteredClientRepository(itClient, feClient, pkceFeClient, resourceServerClient, authServerClient, gatewayServerClient);
	}

	@Bean
	public JWKSource<SecurityContext> jwkSource() {
		KeyPair keyPair = generateRsaKey();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		RSAKey rsaKey = new RSAKey.Builder(publicKey)
				.privateKey(privateKey)
				.keyID(UUID.randomUUID().toString())
				.build();
		JWKSet jwkSet = new JWKSet(rsaKey);
		return new ImmutableJWKSet<>(jwkSet);
	}

	private static KeyPair generateRsaKey() {
		KeyPair keyPair;
		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(2048);
			keyPair = keyPairGenerator.generateKeyPair();
		}
		catch (Exception ex) {
			throw new IllegalStateException(ex);
		}
		return keyPair;
	}

	@Bean
	public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
		return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
	}

	@Bean
	public AuthorizationServerSettings authorizationServerSettings() {
		return AuthorizationServerSettings.builder().build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(UserManagementServerClient userManagementServerClient) {
		EmailPasswordRoleAuthenticationProvider provider = new EmailPasswordRoleAuthenticationProvider(userManagementServerClient);
		ProviderManager providerManager = new ProviderManager(provider);
		providerManager.setEraseCredentialsAfterAuthentication(false);
		return providerManager;
	}

	@Bean
	public OAuth2TokenCustomizer<JwtEncodingContext> jwtTokenCustomizer() {
		return (context) -> {
			var principal = context.getPrincipal();
			OAuth2TokenType tokenType = context.getTokenType();
			if (tokenType.equals(OAuth2TokenType.ACCESS_TOKEN)) {
				Set<String> roles = principal.getAuthorities().stream()
						.map(role -> "ROLE_" + role.getAuthority())
						.collect(Collectors.toSet());
				context.getClaims().claims((claims) -> {
					claims.put("roles", roles);
				});
			} else if (tokenType.equals(new OAuth2TokenType(OidcParameterNames.ID_TOKEN))) {
				UserRole role = principal.getAuthorities().stream()
						.map(GrantedAuthority::getAuthority)
						.map(UserRole::valueOf)
						.findFirst()
						.orElseThrow(() -> new BadCredentialsException("Invalid role")); // Default role if not found
				context.getClaims().claims((claims) -> {
					claims.put("role", role);
				});

			}
		};
	}
}
