package com.university.parking.repository;

import com.university.parking.model.ParkingSlot;
import com.university.parking.model.ParkingViolation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParkingViolationRepository extends JpaRepository<ParkingViolation, Long> {
    List<ParkingViolation> findBySlot(ParkingSlot slot);
}
