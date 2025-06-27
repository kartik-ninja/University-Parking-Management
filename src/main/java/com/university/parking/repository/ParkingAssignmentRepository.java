package com.university.parking.repository;

import com.university.parking.model.ParkingAssignment;
import com.university.parking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParkingAssignmentRepository extends JpaRepository<ParkingAssignment, Long> {
    List<ParkingAssignment> findByUser(User user);
    ParkingAssignment findByUserAndActiveTrue(User user);
}
