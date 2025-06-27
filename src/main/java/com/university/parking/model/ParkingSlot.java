package com.university.parking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "parking_slots")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParkingSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String slotNumber;

    private LocalDateTime availableFrom;
    private LocalDateTime availableTo;

    private boolean booked;

    private String location; // 🔥 NEW

    @ManyToOne
    private User bookedBy; // 🔥 NEW
}
