package com.example.freelance_authserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class LoginController {

	@GetMapping("/login")
	public String showLoginPage() {
		log.info("Displaying login page");
		return "login"; // returns the name of the view for the login page
	}

//	@PostMapping("/login")
//	public void handleLogin() {
//		log.info("Handling login request");
//		// Logic for handling login can be added here
//	}
}
