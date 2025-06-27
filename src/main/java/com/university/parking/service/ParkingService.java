package com.university.parking.service;

import com.university.parking.model.ParkingAssignment;
import com.university.parking.model.ParkingSlot;
import com.university.parking.model.ParkingViolation;
import com.university.parking.model.ParkingRequest;
import com.university.parking.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface ParkingService {

    // Existing
    ParkingSlot createSlot(String slotNumber, LocalDateTime from, LocalDateTime to, String location);
    List<ParkingSlot> getAvailableSlots();
    List<ParkingSlot> getBookedSlots();
    List<ParkingSlot> searchSlotsByLocation(String location);
    List<ParkingSlot> searchSlotsByDate(LocalDateTime from, LocalDateTime to);
    boolean bookSlot(Long slotId, User user);
    boolean cancelSlot(Long slotId, User user);

    // ✅ New (used in Authority/Student/TeacherController)
    ParkingAssignment getActiveAssignmentForUser(Long userId);
    List<ParkingAssignment> getPastAssignmentsForUser(Long userId);
    List<ParkingRequest> getUserRequests(Long userId);
    void createParkingRequest(Long userId, String role, LocalDateTime from, LocalDateTime to);
    void releaseParkingSlot(Long assignmentId);

    // ✅ Admin
    List<ParkingViolation> getAllViolations();
    void reportViolation(ParkingViolation violation);
}
