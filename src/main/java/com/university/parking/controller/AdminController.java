package com.university.parking.controller;

import com.university.parking.model.ParkingAssignment;
import com.university.parking.repository.ParkingAssignmentRepository;
import com.university.parking.service.ParkingService;
import com.university.parking.service.ReportService;
import com.university.parking.service.ViolationService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ViolationService violationService;
    private final ParkingService parkingService;
    private final ParkingAssignmentRepository assignmentRepo;
    private final ReportService  reportService;

    public AdminController(ViolationService violationService,
                           ParkingService parkingService,
                           ParkingAssignmentRepository assignmentRepo,
                           ReportService reportService) {
        this.violationService = violationService;
        this.parkingService = parkingService;
        this.assignmentRepo = assignmentRepo;
        this.reportService = reportService;
    }

    @GetMapping("/dashboard")
    public String adminDashboard(@RequestParam(value = "role", required = false) String role,
                                 @RequestParam(value = "search", required = false) String keyword,
                                 Model model) {

        List<?> assignments;

        if (keyword != null && !keyword.isEmpty()) {
            assignments = parkingService.searchAssignments(keyword);
        } else if (role != null && !role.isEmpty()) {
            assignments = parkingService.getAssignmentsByRole(role);
        } else {
            assignments = parkingService.getAllAssignments();
        }

        model.addAttribute("assignments", assignments);
        model.addAttribute("selectedRole", role);
        model.addAttribute("searchTerm", keyword);
        return "admin/dashboard";
    }

    @GetMapping("/violations")
    public String viewViolations(Model model) {
        model.addAttribute("violations", violationService.getAllViolations());
        return "violations";
    }

    @GetMapping("/reports/bookings")
    public void downloadBookingReport(HttpServletResponse resp) throws Exception {
        List<ParkingAssignment> all = parkingService.getBookedSlots()
                .stream()
                .map(slot -> assignmentRepo.findFirstBySlotAndActiveTrue(slot).orElse(null))
                .filter(a -> a != null)
                .toList();
        reportService.generateBookingReport(all, resp);
    }

    @GetMapping("/reports/violations")
    public void downloadViolationReport(HttpServletResponse resp) throws Exception {
        reportService.generateViolationReport(violationService.getAllViolations(), resp);
    }

}
