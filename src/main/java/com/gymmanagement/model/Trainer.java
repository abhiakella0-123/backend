package com.gymmanagement.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "trainers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Trainer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String firstName;

    @Column(nullable = false, length = 100)
    private String lastName;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(length = 20)
    private String phone;

    @Column(nullable = false, length = 100)
    private String specialization; // e.g. Bodybuilding, Crossfit, Yoga, Cardio/HIIT

    private Integer experienceYears;

    @Builder.Default
    private Double rating = 5.0;

    private Double salary;

    @Column(length = 500)
    private String imageUrl;

    @Builder.Default
    @Column(length = 30)
    private String status = "ACTIVE"; // ACTIVE, ON_LEAVE, INACTIVE
}
