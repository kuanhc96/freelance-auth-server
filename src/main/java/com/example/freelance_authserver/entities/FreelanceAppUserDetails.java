package com.example.freelance_authserver.entities;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;

import com.example.freelance_authserver.enums.UserRole;

@Getter
public class FreelanceAppUserDetails extends User {
	private String userGUID;
	private UserRole role;

	public FreelanceAppUserDetails(String userGUID, String email, UserRole role, String password, Collection<? extends GrantedAuthority> authorities) {
		super(email, password, authorities);
		this.userGUID = userGUID;
		this.role = role;
	}
}
