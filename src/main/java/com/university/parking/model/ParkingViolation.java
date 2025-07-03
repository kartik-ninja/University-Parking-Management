package com.university.parking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "parking_violations")
public class ParkingViolation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private String vehicleNumber;

    private String reportedBy;

    private LocalDateTime reportedAt;

    @ManyToOne
    private ParkingSlot slot;

    @ManyToOne
    private User user;

    public ParkingViolation() {
    }

    public ParkingViolation(Long id, String description, String vehicleNumber, String reportedBy, LocalDateTime reportedAt, ParkingSlot slot, User user) {
        this.id = id;
        this.description = description;
        this.vehicleNumber = vehicleNumber;
        this.reportedBy = reportedBy;
        this.reportedAt = reportedAt;
        this.slot = slot;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(String reportedBy) {
        this.reportedBy = reportedBy;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public LocalDateTime getReportedAt() {
        return reportedAt;
    }

    public void setReportedAt(LocalDateTime reportedAt) {
        this.reportedAt = reportedAt;
    }

    public ParkingSlot getSlot() {
        return slot;
    }

    public void setSlot(ParkingSlot slot) {
        this.slot = slot;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
