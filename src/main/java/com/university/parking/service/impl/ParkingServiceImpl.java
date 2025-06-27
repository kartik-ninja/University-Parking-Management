package com.university.parking.service.impl;

import com.university.parking.model.*;
import com.university.parking.repository.*;
import com.university.parking.service.ParkingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ParkingServiceImpl implements ParkingService {

    private final ParkingSlotRepository slotRepo;
    private final ParkingAssignmentRepository assignmentRepo;
    private final ParkingRequestRepository requestRepo;
    private final ParkingViolationRepository violationRepo;
    private final UserRepository userRepository;

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
    public boolean bookSlot(Long slotId, User user) {
        Optional<ParkingSlot> opt = slotRepo.findById(slotId);
        if (opt.isPresent() && !opt.get().isBooked()) {
            ParkingSlot slot = opt.get();
            slot.setBooked(true);
            slot.setBookedBy(user);

            // create assignment
            ParkingAssignment assignment = new ParkingAssignment();
            assignment.setSlot(slot);
            assignment.setUser(user);
            assignment.setStartTime(slot.getAvailableFrom());
            assignment.setEndTime(slot.getAvailableTo());
            assignment.setActive(true);

            assignmentRepo.save(assignment);
            slotRepo.save(slot);
            return true;
        }
        return false;
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
        return user != null ? assignmentRepo.findByUserAndActiveTrue(user) : null;
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
}
