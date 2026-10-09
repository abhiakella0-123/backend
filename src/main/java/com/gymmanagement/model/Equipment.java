package com.gymmanagement.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "equipment")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(nullable = false, length = 50)
    private String category; // CARDIO, STRENGTH, FREE_WEIGHTS, RECOVERY

    @Builder.Default
    private Integer quantity = 1;

    @Builder.Default
    @Column(nullable = false, length = 30)
    private String conditionStatus = "GOOD"; // EXCELLENT, GOOD, MAINTENANCE_NEEDED, BROKEN

    private LocalDate lastMaintenanceDate;

    private LocalDate purchaseDate;

    @Column(length = 100)
    private String location; // Zone A, Zone B, etc.
}
