package com.example.freelance_authserver.entities;

import com.example.freelance_authserver.enums.UserRole;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;

@Table(name="user_credentials")
@Data
@Builder
public class UserCredentialsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;
    private String userGUID;
    private String email;
    private String password;
    private UserRole role;
}
