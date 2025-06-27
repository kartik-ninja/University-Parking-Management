package com.university.parking.service.impl;

import com.university.parking.model.ParkingSlot;
import com.university.parking.repository.ParkingSlotRepository;
import com.university.parking.service.ParkingSlotService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ParkingSlotServiceImpl implements ParkingSlotService {

    private final ParkingSlotRepository parkingSlotRepository;

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
}
