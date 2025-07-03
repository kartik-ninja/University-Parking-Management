package com.university.parking.controller;

import com.university.parking.model.ParkingSlot;
import com.university.parking.service.ParkingSlotService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/slots")
@RequiredArgsConstructor
public class AdminSlotController {

    private final ParkingSlotService slotService;

    @GetMapping
    public String viewSlots(Model model) {
        model.addAttribute("slots", slotService.listAll());
        return "admin/slots";
    }

    @GetMapping("/new")
    public String createSlotForm(Model model) {
        model.addAttribute("slot", new ParkingSlot());
        return "admin/create-slot";
    }

    @PostMapping
    public String createSlot(@ModelAttribute ParkingSlot slot) {
        slotService.createSlot(slot);
        return "redirect:/admin/slots";
    }

    @PostMapping("/delete/{id}")
    public String deleteSlot(@PathVariable Long id, RedirectAttributes ra) {
        try {
            slotService.deleteSlot(id);
            ra.addFlashAttribute("success", "Slot deleted successfully.");
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/slots";
    }

}
