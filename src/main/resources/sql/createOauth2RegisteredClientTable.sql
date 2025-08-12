CREATE TABLE oauth2_registered_client (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    client_id VARCHAR(255) NOT NULL UNIQUE,
    client_id_issued_at TIMESTAMP NOT NULL,
    client_secret VARCHAR(512),
    client_secret_expires_at TIMESTAMP,
    client_name VARCHAR(255),
    client_authentication_methods VARCHAR(255),
    authorization_grant_types VARCHAR(255),
    redirect_uris TEXT,
    post_logout_redirect_uris TEXT,
    scopes VARCHAR(255),
    client_settings JSON,
    token_settings JSON
);
