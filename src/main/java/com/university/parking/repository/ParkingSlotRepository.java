package com.university.parking.repository;

import com.university.parking.model.ParkingSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, Long> {
    List<ParkingSlot> findByBookedFalse();
    List<ParkingSlot> findByBookedTrue();
    List<ParkingSlot> findByBookedFalseAndLocationContainingIgnoreCase(String location);
    List<ParkingSlot> findByBookedFalseAndAvailableFromBetween(LocalDateTime start, LocalDateTime end);
    List<ParkingSlot> findByBookedFalseAndAvailableFromAfter(LocalDateTime time);
}
