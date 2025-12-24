package com.example.freelance_authserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MVCController {
    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }
}
