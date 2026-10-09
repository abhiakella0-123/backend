package com.gymmanagement.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "members")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String firstName;

    @Column(nullable = false, length = 100)
    private String lastName;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @JsonIgnore
    @Column(length = 60)
    private String passwordHash;

    @Column(length = 20)
    private String phone;

    @Column(length = 20)
    private String gender;

    private LocalDate dateOfBirth;

    @Column(nullable = false)
    private LocalDate joinDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "membership_plan_id")
    private MembershipPlan membershipPlan;

    private LocalDate membershipStartDate;

    private LocalDate membershipEndDate;

    private LocalDateTime trialEndsAt;

    @Column(columnDefinition = "TEXT")
    private String weeklyWorkoutPlan;

    @Builder.Default
    @Column(nullable = false, length = 30)
    private String status = "ACTIVE"; // ACTIVE, EXPIRED, PENDING, FROZEN

    @Column(length = 100)
    private String emergencyContact;

    @Column(length = 500)
    private String notes;

    @Column(length = 500)
    private String avatarUrl;
}
