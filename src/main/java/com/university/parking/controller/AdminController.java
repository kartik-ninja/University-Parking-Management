package com.university.parking.controller;

import com.university.parking.model.ParkingViolation;
import com.university.parking.service.ParkingService;
import com.university.parking.service.ViolationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ViolationService violationService;

    @GetMapping("/dashboard")
    public String adminDashboard() {
        return "admin/dashboard";
    }

    @GetMapping("/violations")
    public String viewViolations(Model model) {
        model.addAttribute("violations", violationService.getAllViolations());
        return "admin/violations";
    }

    @PostMapping("/violations/add")
    public String addViolation(@ModelAttribute ParkingViolation violation) {
        violationService.reportViolation(violation);
        return "redirect:/admin/violations";
    }
}
