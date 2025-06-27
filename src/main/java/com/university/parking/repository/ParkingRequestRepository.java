package com.university.parking.repository;

import com.university.parking.model.ParkingRequest;
import com.university.parking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParkingRequestRepository extends JpaRepository<ParkingRequest, Long> {
    List<ParkingRequest> findByUser(User user);
}
