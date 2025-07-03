package com.university.parking.service;

import com.university.parking.model.ParkingAssignment;
import com.university.parking.model.ParkingViolation;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

public interface ReportService {
    void generateBookingReport(List<ParkingAssignment> bookings, HttpServletResponse response) throws Exception;
    void generateViolationReport(List<ParkingViolation> violations, HttpServletResponse response) throws Exception;
}
