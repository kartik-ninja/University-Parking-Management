package com.university.parking.controller;

import com.university.parking.model.User;
import com.university.parking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserService userService;

    @GetMapping
    public String viewProfile(Authentication auth, Model model) {
        User user = userService.getUserByEmail(auth.getName());
        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping("/update")
    public String updateProfile(@ModelAttribute User updated, Authentication auth) {
        User existing = userService.getUserByEmail(auth.getName());
        existing.setFirstName(updated.getFirstName());
        existing.setLastName(updated.getLastName());
        userService.update(existing);
        return "redirect:/profile";
    }
}
