package com.university.parking.service;

import com.university.parking.model.ParkingSlot;

import java.util.List;

public interface ParkingSlotService {
    ParkingSlot createSlot(ParkingSlot slot);
    List<ParkingSlot> listAll();
    List<ParkingSlot> listAvailable();
}
