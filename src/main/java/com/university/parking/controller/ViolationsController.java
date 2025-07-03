package com.university.parking.controller;

import com.university.parking.model.ParkingViolation;
import com.university.parking.service.ParkingService;
import com.university.parking.service.ViolationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/violations")
@RequiredArgsConstructor
public class ViolationsController {

    private final ViolationService violationService;
    private final ParkingService parkingService;

    @GetMapping
    public String showViolations(Model model) {
        model.addAttribute("violations", violationService.getAllViolations());
        model.addAttribute("availableSlots", parkingService.getAvailableSlots());
        model.addAttribute("violation", new ParkingViolation());
        return "violations";
    }

    @PostMapping("/add")
    public String addViolation(@ModelAttribute ParkingViolation violation, @RequestParam Long slotId) {
        violation.setSlot(parkingService.findSlotById(slotId));
        violation.setReportedAt(java.time.LocalDateTime.now());
        violationService.reportViolation(violation);
        return "redirect:/violations";
    }
}
