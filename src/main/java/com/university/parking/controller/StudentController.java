package com.university.parking.controller;

import com.university.parking.model.User;
import com.university.parking.service.ParkingService;
import com.university.parking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {

    private final ParkingService parkingService;
    private final UserService userService;

    @GetMapping("/dashboard")
    public String dashboard(Authentication auth, Model model) {
        User user = userService.getUserByEmail(auth.getName());

        model.addAttribute("user", user);
        model.addAttribute("currentAssignment", parkingService.getActiveAssignmentForUser(user.getId()));
        model.addAttribute("requests", parkingService.getUserRequests(user.getId()));
        model.addAttribute("pastAssignments", parkingService.getPastAssignmentsForUser(user.getId()));

        return "student/dashboard";
    }

    @PostMapping("/request")
    public String requestSlot(Authentication auth,
                              @RequestParam String startDate,
                              @RequestParam String endDate) {
        User user = userService.getUserByEmail(auth.getName());

        parkingService.createParkingRequest(user.getId(), "STUDENT",
                LocalDateTime.parse(startDate),
                LocalDateTime.parse(endDate));
        return "redirect:/student/dashboard";
    }

    @PostMapping("/release/{id}")
    public String release(@PathVariable Long id) {
        parkingService.releaseParkingSlot(id);
        return "redirect:/student/dashboard";
    }
}
