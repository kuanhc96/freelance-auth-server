package com.example.freelance_authserver.entities;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import com.example.freelance_authserver.enums.Gender;
import com.example.freelance_authserver.enums.UserRole;
import com.example.freelance_authserver.enums.UserStatus;

@Getter
@Builder
public class CreateUserRequest {
	private String email;
	private String password;
	private UserRole role;
	private UserStatus status;
	private String name;
	private LocalDate birthday;
	private Gender gender;
	private String description;
	private String profilePicture;
}
