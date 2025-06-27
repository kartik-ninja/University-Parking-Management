package com.university.parking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "parking_violations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
}
