package com.university.parking.service;

import com.university.parking.model.ParkingViolation;

import java.util.List;

public interface ViolationService {
    void reportViolation(ParkingViolation violation);
    List<ParkingViolation> getAllViolations();
}
