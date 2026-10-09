package com.gymmanagement.service;

import com.gymmanagement.model.Trainer;
import com.gymmanagement.repository.TrainerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainerService {

    private final TrainerRepository trainerRepository;

    public List<Trainer> getAllTrainers() {
        return trainerRepository.findAll();
    }

    public List<Trainer> getActiveTrainers() {
        return trainerRepository.findByStatus("ACTIVE");
    }

    public Trainer getTrainerById(Long id) {
        return trainerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trainer not found with id: " + id));
    }

    public Trainer createTrainer(Trainer trainer) {
        if (trainer.getStatus() == null) {
            trainer.setStatus("ACTIVE");
        }
        return trainerRepository.save(trainer);
    }

    public Trainer updateTrainer(Long id, Trainer details) {
        Trainer trainer = getTrainerById(id);
        trainer.setFirstName(details.getFirstName());
        trainer.setLastName(details.getLastName());
        trainer.setEmail(details.getEmail());
        trainer.setPhone(details.getPhone());
        trainer.setSpecialization(details.getSpecialization());
        trainer.setExperienceYears(details.getExperienceYears());
        trainer.setRating(details.getRating());
        trainer.setSalary(details.getSalary());
        trainer.setImageUrl(details.getImageUrl());
        trainer.setStatus(details.getStatus());
        return trainerRepository.save(trainer);
    }

    public void deleteTrainer(Long id) {
        trainerRepository.deleteById(id);
    }
}
