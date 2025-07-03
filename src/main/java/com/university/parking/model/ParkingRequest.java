package com.university.parking.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "parking_requests")
public class ParkingRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private String role; // STUDENT, TEACHER, etc.

    @Column(name = "requested_from")
    private LocalDateTime requestedFrom;

    @Column(name = "requested_to")
    private LocalDateTime requestedTo;

    @Column(name = "requested_at")
    private LocalDateTime requestedAt;

    private String status; // PENDING, APPROVED, REJECTED

    public ParkingRequest() {
    }

    public ParkingRequest(Long id, User user, String role, LocalDateTime requestedFrom, LocalDateTime requestedTo, LocalDateTime requestedAt, String status) {
        this.id = id;
        this.user = user;
        this.role = role;
        this.requestedFrom = requestedFrom;
        this.requestedTo = requestedTo;
        this.requestedAt = requestedAt;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public LocalDateTime getRequestedFrom() {
        return requestedFrom;
    }

    public void setRequestedFrom(LocalDateTime requestedFrom) {
        this.requestedFrom = requestedFrom;
    }

    public LocalDateTime getRequestedTo() {
        return requestedTo;
    }

    public void setRequestedTo(LocalDateTime requestedTo) {
        this.requestedTo = requestedTo;
    }

    public LocalDateTime getRequestedAt() {
        return requestedAt;
    }

    public void setRequestedAt(LocalDateTime requestedAt) {
        this.requestedAt = requestedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
