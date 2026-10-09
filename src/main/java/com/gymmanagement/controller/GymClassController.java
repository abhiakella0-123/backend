package com.gymmanagement.controller;

import com.gymmanagement.model.GymClass;
import com.gymmanagement.service.GymClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/classes")
@RequiredArgsConstructor

public class GymClassController {

    private final GymClassService gymClassService;

    @GetMapping
    public ResponseEntity<List<GymClass>> getAllClasses() {
        return ResponseEntity.ok(gymClassService.getAllClasses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GymClass> getClassById(@PathVariable Long id) {
        return ResponseEntity.ok(gymClassService.getClassById(id));
    }

    @PostMapping
    public ResponseEntity<GymClass> createClass(@RequestBody GymClass gymClass) {
        return ResponseEntity.ok(gymClassService.createClass(gymClass));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GymClass> updateClass(@PathVariable Long id, @RequestBody GymClass gymClass) {
        return ResponseEntity.ok(gymClassService.updateClass(id, gymClass));
    }

    @PostMapping("/{id}/enroll")
    public ResponseEntity<GymClass> enrollMember(@PathVariable Long id) {
        return ResponseEntity.ok(gymClassService.enrollMember(id));
    }

    @PostMapping("/{id}/cancel-enrollment")
    public ResponseEntity<GymClass> cancelEnrollment(@PathVariable Long id) {
        return ResponseEntity.ok(gymClassService.cancelEnrollment(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClass(@PathVariable Long id) {
        gymClassService.deleteClass(id);
        return ResponseEntity.noContent().build();
    }
}
