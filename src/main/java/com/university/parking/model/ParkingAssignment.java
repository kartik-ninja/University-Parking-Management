package com.university.parking.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "parking_assignments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParkingAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private ParkingSlot slot;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private boolean active = true;
}
