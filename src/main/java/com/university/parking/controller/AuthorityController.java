package com.university.parking.controller;

import com.university.parking.model.ParkingAssignment;
import com.university.parking.model.ParkingSlot;
import com.university.parking.model.ParkingViolation;
import com.university.parking.model.User;
import com.university.parking.service.ParkingService;
import com.university.parking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/authority")
public class AuthorityController {

    private final ParkingService parkingService;
    private final UserService userService;

    public  AuthorityController(ParkingService parkingService, UserService userService) {
        this.parkingService = parkingService;
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication auth, Model model) {
        User user = userService.getUserByEmail(auth.getName());
        List<ParkingAssignment> current = parkingService.getAllActiveAssignmentsForUser(user.getId());
        List<ParkingSlot> available = parkingService.getAvailableSlots();
        List<ParkingViolation> violations = parkingService.getAllViolations();

        model.addAttribute("user", user);
        model.addAttribute("currentAssignments", current);
        model.addAttribute("availableSlots", available);
        model.addAttribute("violations", violations);

        return "authority/dashboard";
    }

    @PostMapping("/book/{slotId}")
    public String bookSlot(@PathVariable Long slotId,
                           Authentication auth,
                           RedirectAttributes ra) {
        User user = userService.getUserByEmail(auth.getName());
        try {
            parkingService.bookSlot(slotId, user);
            ra.addFlashAttribute("success", "Slot booked successfully!");
        } catch (IllegalStateException ex) {
            ra.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/authority/dashboard";
    }

    @PostMapping("/release/{id}")
    public String release(@PathVariable Long id, RedirectAttributes ra) {
        parkingService.releaseParkingSlot(id);
        ra.addFlashAttribute("success", "Slot released.");
        return "redirect:/authority/dashboard";
    }

    @GetMapping("/history")
    public String viewHistory(Authentication auth, Model model) {
        User user = userService.getUserByEmail(auth.getName());
        List<ParkingAssignment> history = parkingService.getPastAssignmentsForUser(user.getId());

        model.addAttribute("assignments", history);
        model.addAttribute("dashboardLink", "/authority/dashboard");
        return "booking-history"; // shared template
    }
}
