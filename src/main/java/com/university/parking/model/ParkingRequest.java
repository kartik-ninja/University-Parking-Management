package com.university.parking.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "parking_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
}
