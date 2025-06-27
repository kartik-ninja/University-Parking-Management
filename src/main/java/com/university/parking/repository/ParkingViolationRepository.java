package com.university.parking.repository;

import com.university.parking.model.ParkingViolation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingViolationRepository extends JpaRepository<ParkingViolation, Long> {
}
