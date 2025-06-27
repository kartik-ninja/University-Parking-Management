package com.university.parking.controller;

import com.university.parking.model.User;
import com.university.parking.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final UserService userService;

    @GetMapping("/dashboard")
    public void dashboardRedirect(Authentication auth, HttpServletResponse response) throws IOException {
        User user = userService.getUserByEmail(auth.getName());

        String role = user.getRoles().stream().map(r -> r.getName()).findFirst().orElse("");

        switch (role) {
            case "ROLE_ADMIN" -> response.sendRedirect("/admin/dashboard");
            case "ROLE_STUDENT" -> response.sendRedirect("/student/dashboard");
            case "ROLE_TEACHER" -> response.sendRedirect("/teacher/dashboard");
            case "ROLE_AUTHORITY" -> response.sendRedirect("/authority/dashboard");
            default -> response.sendRedirect("/login?error");
        }
    }
}
