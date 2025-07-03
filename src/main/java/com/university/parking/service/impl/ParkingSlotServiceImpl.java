package com.university.parking.service.impl;

import com.university.parking.model.ParkingAssignment;
import com.university.parking.model.ParkingSlot;
import com.university.parking.model.ParkingViolation;
import com.university.parking.repository.ParkingAssignmentRepository;
import com.university.parking.repository.ParkingSlotRepository;
import com.university.parking.repository.ParkingViolationRepository;
import com.university.parking.service.ParkingSlotService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ParkingSlotServiceImpl implements ParkingSlotService {

    private final ParkingSlotRepository parkingSlotRepository;
    private final ParkingAssignmentRepository parkingAssignmentRepository;
    private final ParkingViolationRepository  parkingViolationRepository;

    @Override
    public ParkingSlot createSlot(ParkingSlot slot) {
        slot.setBooked(false);
        return parkingSlotRepository.save(slot);
    }

    @Override
    public List<ParkingSlot> listAll() {
        return parkingSlotRepository.findAll();
    }

    @Override
    public List<ParkingSlot> listAvailable() {
        return parkingSlotRepository.findByBookedFalseAndAvailableFromAfter(LocalDateTime.now());
    }

    @Transactional
    @Override
    public void deleteSlot(Long slotId) {
        ParkingSlot slot = parkingSlotRepository.findById(slotId)
                .orElseThrow(() -> new IllegalArgumentException("Slot not found"));

        // 1. Delete all violations for this slot
        List<ParkingViolation> violations = parkingViolationRepository.findBySlot(slot);
        parkingViolationRepository.deleteAll(violations);

        // 2. Delete all assignments for this slot
        List<ParkingAssignment> assignments = parkingAssignmentRepository.findBySlot(slot);
        parkingAssignmentRepository.deleteAll(assignments);

        // 3. Finally, delete the slot
        parkingSlotRepository.delete(slot);
    }

}
