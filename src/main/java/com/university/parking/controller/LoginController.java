package com.university.parking.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/")
    public String startPage() {
        return "auth/login";
    }

    @GetMapping("/auth/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/login")
    public String loginRedirect() {
        return "redirect:/auth/login";
    }
}
