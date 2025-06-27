package com.university.parking.controller;

import com.university.parking.model.ParkingSlot;
import com.university.parking.model.User;
import com.university.parking.service.ParkingService;
import com.university.parking.service.ParkingSlotService;
import com.university.parking.service.UserService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/slots")
public class SlotController {

    private final ParkingService parkingService;
    private final UserService userService;
    private final ParkingSlotService slotService;

    public SlotController(ParkingService parkingService, UserService userService,  ParkingSlotService slotService) {
        this.parkingService = parkingService;
        this.userService = userService;
        this.slotService = slotService;
    }

    @GetMapping("/new")
    public String showCreateForm() {
        return "slots/create-slot";
    }

    @PostMapping("/new")
    public String createSlot(@RequestParam String slotNumber,
                             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
                             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
                             @RequestParam String location) {
        parkingService.createSlot(slotNumber, from, to, location);
        return "redirect:/slots/new?success";
    }

    @GetMapping("/available")
    public String viewAvailable(@RequestParam(required = false) String location,
                                @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
                                @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
                                Model model) {

        List<ParkingSlot> slots;
        if (location != null && !location.isBlank()) {
            slots = parkingService.searchSlotsByLocation(location);
        } else if (from != null && to != null) {
            slots = parkingService.searchSlotsByDate(from, to);
        } else {
            slots = parkingService.getAvailableSlots();
        }

        model.addAttribute("slots", slots);
        return "slots/available-slots";
    }

    @PostMapping("/book/{id}")
    public String bookSlot(@PathVariable Long id, Principal principal) {
        User user = userService.getUserByEmail(principal.getName());
        parkingService.bookSlot(id, user);
        return "redirect:/slots/available?booked";
    }

    @PostMapping("/cancel/{id}")
    public String cancelSlot(@PathVariable Long id, Principal principal) {
        User user = userService.getUserByEmail(principal.getName());
        parkingService.cancelSlot(id, user);
        return "redirect:/dashboard?cancelled";
    }

    @GetMapping("/slots/all")
    public String viewAllSlots(Model model) {
        model.addAttribute("slots", slotService.listAll());
        return "slots/all";
    }
}
