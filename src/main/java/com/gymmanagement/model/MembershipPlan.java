package com.gymmanagement.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "membership_plans")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MembershipPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private Integer durationMonths;

    @Column(nullable = false)
    private Double price;

    @Column(length = 500)
    private String features; // Comma-separated or bullet list of perks

    @Builder.Default
    private Boolean active = true;
}
