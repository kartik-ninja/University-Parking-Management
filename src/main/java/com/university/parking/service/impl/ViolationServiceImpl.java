package com.university.parking.service.impl;

import com.university.parking.model.ParkingViolation;
import com.university.parking.repository.ParkingViolationRepository;
import com.university.parking.service.ViolationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ViolationServiceImpl implements ViolationService {

    private final ParkingViolationRepository violationRepository;

    @Override
    public void reportViolation(ParkingViolation violation) {
        violationRepository.save(violation);
    }

    @Override
    public List<ParkingViolation> getAllViolations() {
        return violationRepository.findAll();
    }
}
