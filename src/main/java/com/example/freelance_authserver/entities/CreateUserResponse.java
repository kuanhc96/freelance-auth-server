package com.example.freelance_authserver.entities;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

import com.example.freelance_authserver.enums.Gender;
import com.example.freelance_authserver.enums.UserStatus;

@Data
@Builder
public class CreateUserResponse {
	private String userGUID;
	private String name;
	private String email;
	private LocalDate birthday;
	private Gender gender;
	private String description;
	private UserStatus status;
}
