package com.university.parking.repository;

import com.university.parking.model.ParkingAssignment;
import com.university.parking.model.ParkingSlot;
import com.university.parking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParkingAssignmentRepository extends JpaRepository<ParkingAssignment, Long> {
    List<ParkingAssignment> findByUser(User user);
    List<ParkingAssignment> findAllByUserAndActiveTrue(User user);
    Optional<ParkingAssignment> findFirstByUserAndActiveTrue(User user);

    // 🔍 Filter by role name
    @Query("SELECT p FROM ParkingAssignment p JOIN p.user u JOIN u.roles r WHERE r.name = :role")
    List<ParkingAssignment> findByUserRole(@Param("role") String role);

    // 🔍 Search by slot number or user name
    @Query("""
           SELECT p FROM ParkingAssignment p
           WHERE LOWER(p.slot.slotNumber) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(p.user.firstName) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(p.user.lastName) LIKE LOWER(CONCAT('%', :keyword, '%'))
           """)
    List<ParkingAssignment> searchByKeyword(@Param("keyword") String keyword);

    Optional<ParkingAssignment> findFirstBySlotAndActiveTrue(ParkingSlot slot);
}
