package com.university.parking.controller;

import com.university.parking.dto.UserRegistrationDto;
import com.university.parking.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegistrationController {

    private final UserService userService;

    @GetMapping
    public String showForm(Model model) {
        model.addAttribute("user", new UserRegistrationDto());
        return "auth/register";
    }

    @PostMapping
    public String register(@ModelAttribute("user") @Valid UserRegistrationDto dto,
                           BindingResult result) {
        if (result.hasErrors()) return "auth/register";

        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            result.rejectValue("confirmPassword", null, "Passwords do not match");
            return "auth/register";
        }

        if (userService.emailExists(dto.getEmail())) {
            result.rejectValue("email", null, "Email already in use");
            return "auth/register";
        }

        if (userService.universityIdExists(dto.getUniversityId())) {
            result.rejectValue("universityId", null, "University ID already used");
            return "auth/register";
        }

        userService.save(dto);
        return "redirect:/login?registered";
    }
}
