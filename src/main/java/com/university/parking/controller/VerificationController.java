package com.university.parking.controller;

import com.university.parking.model.User;
import com.university.parking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class VerificationController {

    private final UserService userService;

    @GetMapping("/verify")
    public String showVerifyPage(@RequestParam(required = false) String email, Model model) {
        model.addAttribute("email", email); // ✅ Pass email to the form
        return "verify";
    }

    @PostMapping("/verify")
    public String verifyEmail(@RequestParam String email, @RequestParam String code, RedirectAttributes ra) {
        User user = userService.getUserByEmail(email);
        if (user != null && user.getVerificationCode().equals(code)) {
            user.setEnabled(true);
            user.setVerificationCode(null); // clear it
            userService.save(user); // update user
            ra.addFlashAttribute("success", "Email verified successfully!");
            return "redirect:/auth/login";
        } else {
            ra.addFlashAttribute("error", "Invalid verification code.");
            return "redirect:/verify";
        }
    }
}

