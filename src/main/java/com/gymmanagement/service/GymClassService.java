package com.gymmanagement.service;

import com.gymmanagement.model.GymClass;
import com.gymmanagement.model.Trainer;
import com.gymmanagement.repository.GymClassRepository;
import com.gymmanagement.repository.TrainerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GymClassService {

    private final GymClassRepository classRepository;
    private final TrainerRepository trainerRepository;

    public List<GymClass> getAllClasses() {
        return classRepository.findAll();
    }

    public GymClass getClassById(Long id) {
        return classRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gym Class not found with id: " + id));
    }

    public GymClass createClass(GymClass gymClass) {
        if (gymClass.getTrainer() != null && gymClass.getTrainer().getId() != null) {
            Trainer trainer = trainerRepository.findById(gymClass.getTrainer().getId()).orElse(null);
            gymClass.setTrainer(trainer);
        }
        if (gymClass.getEnrolledCount() == null) {
            gymClass.setEnrolledCount(0);
        }
        return classRepository.save(gymClass);
    }

    public GymClass updateClass(Long id, GymClass details) {
        GymClass gymClass = getClassById(id);
        gymClass.setClassName(details.getClassName());
        gymClass.setDescription(details.getDescription());
        gymClass.setScheduleTime(details.getScheduleTime());
        gymClass.setDaysOfWeek(details.getDaysOfWeek());
        gymClass.setDurationMinutes(details.getDurationMinutes());
        gymClass.setCapacity(details.getCapacity());
        gymClass.setRoom(details.getRoom());

        if (details.getTrainer() != null && details.getTrainer().getId() != null) {
            Trainer trainer = trainerRepository.findById(details.getTrainer().getId()).orElse(null);
            gymClass.setTrainer(trainer);
        }
        return classRepository.save(gymClass);
    }

    @Transactional
    public GymClass enrollMember(Long classId) {
        GymClass gymClass = getClassById(classId);
        if (gymClass.getEnrolledCount() >= gymClass.getCapacity()) {
            throw new RuntimeException("Class is already at maximum capacity (" + gymClass.getCapacity() + ")");
        }
        gymClass.setEnrolledCount(gymClass.getEnrolledCount() + 1);
        return classRepository.save(gymClass);
    }

    @Transactional
    public GymClass cancelEnrollment(Long classId) {
        GymClass gymClass = getClassById(classId);
        if (gymClass.getEnrolledCount() > 0) {
            gymClass.setEnrolledCount(gymClass.getEnrolledCount() - 1);
        }
        return classRepository.save(gymClass);
    }

    public void deleteClass(Long id) {
        classRepository.deleteById(id);
    }
}
