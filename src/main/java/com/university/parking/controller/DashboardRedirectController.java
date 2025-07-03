package com.university.parking.controller;

import com.university.parking.model.User;
import com.university.parking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardRedirectController {

    private final UserService userService;

    public  DashboardRedirectController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    public String dashboardRedirect(Authentication authentication) {
        User user = userService.getUserByEmail(authentication.getName());
        String role = user.getRoles().stream().findFirst().get().getName();

        return switch (role) {
            case "ROLE_TEACHER" -> "redirect:/teacher/dashboard";
            case "ROLE_STUDENT" -> "redirect:/student/dashboard";
            case "ROLE_ADMIN" -> "redirect:/admin/dashboard";
            case "ROLE_AUTHORITY" -> "redirect:/authority/dashboard";
            default -> "redirect:/auth/login?unauthorized";
        };
    }
}
