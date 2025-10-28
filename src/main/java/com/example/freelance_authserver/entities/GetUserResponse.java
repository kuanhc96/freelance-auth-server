package com.example.freelance_authserver.entities;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

import com.example.freelance_authserver.enums.Gender;
import com.example.freelance_authserver.enums.UserRole;


@Builder
@Data
public class GetUserResponse {
	private String userGUID;
	private String name;
	private String email;
	private UserRole role;
	private Gender gender;
	private String description;
	private LocalDate birthday;
	private String profilePicture;
}
