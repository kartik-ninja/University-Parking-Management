package com.university.parking.service.impl;

import com.university.parking.model.*;
import com.university.parking.repository.*;
import com.university.parking.service.EmailService;
import com.university.parking.service.ParkingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ParkingServiceImpl implements ParkingService {

    private final ParkingSlotRepository slotRepo;
    private final ParkingAssignmentRepository assignmentRepo;
    private final ParkingRequestRepository requestRepo;
    private final ParkingViolationRepository violationRepo;
    private final UserRepository userRepository;

    public ParkingServiceImpl(ParkingSlotRepository slotRepo, ParkingAssignmentRepository assignmentRepo, ParkingRequestRepository requestRepo, ParkingViolationRepository violationRepo, UserRepository userRepository) {
        this.slotRepo = slotRepo;
        this.assignmentRepo = assignmentRepo;
        this.requestRepo = requestRepo;
        this.violationRepo = violationRepo;
        this.userRepository = userRepository;
    }

    @Autowired
    private EmailService emailService;

    @Override
    public ParkingSlot createSlot(String slotNumber, LocalDateTime from, LocalDateTime to, String location) {
        ParkingSlot slot = new ParkingSlot();
        slot.setSlotNumber(slotNumber);
        slot.setAvailableFrom(from);
        slot.setAvailableTo(to);
        slot.setLocation(location);
        slot.setBooked(false);
        return slotRepo.save(slot);
    }

    @Override
    public List<ParkingSlot> getAvailableSlots() {
        return slotRepo.findByBookedFalse();
    }

    @Override
    public List<ParkingSlot> getBookedSlots() {
        return slotRepo.findByBookedTrue();
    }

    @Override
    public List<ParkingSlot> searchSlotsByLocation(String location) {
        return slotRepo.findByBookedFalseAndLocationContainingIgnoreCase(location);
    }

    @Override
    public List<ParkingSlot> searchSlotsByDate(LocalDateTime from, LocalDateTime to) {
        return slotRepo.findByBookedFalseAndAvailableFromBetween(from, to);
    }

    @Override
    public void bookSlot(Long slotId, User user) {
        List<ParkingAssignment> activeAssignments = assignmentRepo.findAllByUserAndActiveTrue(user);

        // Block multiple bookings for STUDENT or TEACHER
        if (!user.getRoles().stream().anyMatch(r -> r.getName().equals("AUTHORITY")) && !activeAssignments.isEmpty()) {
            throw new IllegalStateException("You can only book one active slot at a time.");
        }

        Optional<ParkingSlot> opt = slotRepo.findById(slotId);
        if (opt.isPresent() && !opt.get().isBooked()) {
            ParkingSlot slot = opt.get();
            slot.setBooked(true);
            slot.setBookedBy(user);

            ParkingAssignment assignment = new ParkingAssignment();
            assignment.setSlot(slot);
            assignment.setUser(user);
            assignment.setStartTime(slot.getAvailableFrom());
            assignment.setEndTime(slot.getAvailableTo());
            assignment.setActive(true);

            assignmentRepo.save(assignment);
            slotRepo.save(slot);

            // ✅ Send Email
            String subject = "🅿️ Slot Booking Confirmed";
            String body = "Dear " + user.getFirstName() + ",\n\n" +
                    "Your parking slot " + slot.getSlotNumber() + " has been successfully booked from " +
                    slot.getAvailableFrom() + " to " + slot.getAvailableTo() + ".\n\n" +
                    "Thank you,\nUniversity Parking Team";

            emailService.sendEmail(user.getEmail(), subject, body);
        } else {
            throw new IllegalStateException("Slot is already booked or does not exist.");
        }
    }


    @Override
    public boolean cancelSlot(Long slotId, User user) {
        Optional<ParkingSlot> opt = slotRepo.findById(slotId);
        if (opt.isPresent()) {
            ParkingSlot slot = opt.get();
            if (slot.getBookedBy() != null && slot.getBookedBy().getId().equals(user.getId())) {
                slot.setBooked(false);
                slot.setBookedBy(null);
                slotRepo.save(slot);
                return true;
            }
        }
        return false;
    }

    @Override
    public ParkingAssignment getActiveAssignmentForUser(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return null;

        return assignmentRepo.findFirstByUserAndActiveTrue(user).orElse(null);
    }

    @Override
    public List<ParkingAssignment> getAllActiveAssignmentsForUser(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        return user == null ? List.of() : assignmentRepo.findAllByUserAndActiveTrue(user);
    }

    @Override
    public List<ParkingAssignment> getPastAssignmentsForUser(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        return user != null ? assignmentRepo.findByUser(user).stream().filter(a -> !a.isActive()).toList() : List.of();
    }

    @Override
    public List<ParkingRequest> getUserRequests(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        return user != null ? requestRepo.findByUser(user) : List.of();
    }

    @Override
    public void createParkingRequest(Long userId, String role, LocalDateTime from, LocalDateTime to) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            ParkingRequest req = new ParkingRequest();
            req.setUser(user);
            req.setRequestedAt(LocalDateTime.now());
            req.setRequestedFrom(from);
            req.setRequestedTo(to);
            req.setStatus("PENDING");
            req.setRole(role);
            requestRepo.save(req);
        }
    }

    @Override
    public void releaseParkingSlot(Long assignmentId) {
        Optional<ParkingAssignment> opt = assignmentRepo.findById(assignmentId);
        if (opt.isPresent()) {
            ParkingAssignment assignment = opt.get();
            assignment.setActive(false);
            assignmentRepo.save(assignment);

            ParkingSlot slot = assignment.getSlot();
            slot.setBooked(false);
            slot.setBookedBy(null);
            slotRepo.save(slot);
        }
    }

    @Override
    public List<ParkingViolation> getAllViolations() {
        return violationRepo.findAll();
    }

    @Override
    public void reportViolation(ParkingViolation violation) {
        violation.setReportedAt(LocalDateTime.now());
        violationRepo.save(violation);
    }

    @Override
    public ParkingSlot findSlotById(Long slotId) {
        return slotRepo.findById(slotId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid slot ID"));
    }

    @Override
    public List<ParkingAssignment> getAllAssignments() {
        return assignmentRepo.findAll();
    }

    @Override
    public List<ParkingAssignment> getAssignmentsByRole(String role) {
        return assignmentRepo.findByUserRole("ROLE_" + role.toUpperCase());
    }

    @Override
    public List<ParkingAssignment> searchAssignments(String keyword) {
        return assignmentRepo.searchByKeyword(keyword);
    }

}
