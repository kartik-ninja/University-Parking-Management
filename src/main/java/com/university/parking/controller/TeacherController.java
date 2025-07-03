package com.university.parking.controller;

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

import java.time.LocalDateTime;

@Controller
@RequestMapping("/teacher")
@RequiredArgsConstructor
public class TeacherController {

    private final ParkingService parkingService;
    private final UserService userService;

    @GetMapping("/dashboard")
    public String dashboard(Authentication auth, Model model) {
        User user = userService.getUserByEmail(auth.getName());
        model.addAttribute("user", user);
        model.addAttribute("currentAssignment", parkingService.getActiveAssignmentForUser(user.getId()));
        model.addAttribute("pastAssignments", parkingService.getPastAssignmentsForUser(user.getId()));
        model.addAttribute("availableSlots", parkingService.getAvailableSlots());
        model.addAttribute("violation", new ParkingViolation());
        return "teacher/dashboard";
    }

    @PostMapping("/book/{slotId}")
    public String bookSlot(@PathVariable Long slotId, Authentication auth, RedirectAttributes ra) {
        User user = userService.getUserByEmail(auth.getName());
        try {
            parkingService.bookSlot(slotId, user);
            ra.addFlashAttribute("success", "Slot booked successfully!");
        } catch (IllegalStateException ex) {
            ra.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/teacher/dashboard";
    }

    @PostMapping("/request")
    public String requestSlot(Authentication auth,
                              @RequestParam String startDate,
                              @RequestParam String endDate) {
        User user = userService.getUserByEmail(auth.getName());

        parkingService.createParkingRequest(user.getId(), "TEACHER",
                LocalDateTime.parse(startDate),
                LocalDateTime.parse(endDate));
        return "redirect:/teacher/dashboard";
    }

    @PostMapping("/release/{id}")
    public String release(@PathVariable Long id) {
        parkingService.releaseParkingSlot(id);
        return "redirect:/teacher/dashboard";
    }

    @PostMapping("/report-violation")
    public String reportViolation(Authentication auth,
                                  @ModelAttribute ParkingViolation violation,
                                  @RequestParam Long slotId,
                                  RedirectAttributes ra) {
        User user = userService.getUserByEmail(auth.getName());
        violation.setReportedBy(user.getFirstName() + " " + user.getLastName());
        violation.setSlot(parkingService.findSlotById(slotId));
        violation.setUser(user);
        violation.setReportedAt(java.time.LocalDateTime.now());
        parkingService.reportViolation(violation);
        ra.addFlashAttribute("success", "Violation reported successfully!");
        return "redirect:/teacher/dashboard"; // ✅ back to teacher dashboard
    }

    @GetMapping("/report-form")
    public String showViolationForm(Authentication auth, Model model) {
        User user = userService.getUserByEmail(auth.getName());
        model.addAttribute("violation", new ParkingViolation());
        model.addAttribute("availableSlots", parkingService.getAvailableSlots());
        model.addAttribute("formAction", "/teacher/report-violation"); // or /teacher/...
        model.addAttribute("dashboardLink", "/teacher/dashboard");     // or /teacher/...
        return "report-violation";
    }

    @GetMapping("/history")
    public String showTeacherHistory(Authentication auth, Model model) {
        User user = userService.getUserByEmail(auth.getName());
        model.addAttribute("assignments", parkingService.getPastAssignmentsForUser(user.getId()));
        model.addAttribute("dashboardLink", "/teacher/dashboard");
        return "booking-history";
    }
}
