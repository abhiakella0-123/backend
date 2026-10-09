package com.gymmanagement.repository;

import com.gymmanagement.model.GymClass;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GymClassRepository extends JpaRepository<GymClass, Long> {
    List<GymClass> findByTrainerId(Long trainerId);
}
