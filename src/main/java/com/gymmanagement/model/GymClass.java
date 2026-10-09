package com.gymmanagement.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "gym_classes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GymClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String className;

    @Column(length = 500)
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "trainer_id")
    private Trainer trainer;

    @Column(nullable = false, length = 50)
    private String scheduleTime; // e.g. "06:30 AM - 07:30 AM"

    @Column(length = 100)
    private String daysOfWeek; // e.g. "Mon, Wed, Fri"

    @Column(nullable = false)
    private Integer durationMinutes;

    @Column(nullable = false)
    private Integer capacity;

    @Builder.Default
    private Integer enrolledCount = 0;

    @Column(length = 100)
    private String room; // e.g. "Studio A", "Cardio Zone", "Spinning Room"
}
