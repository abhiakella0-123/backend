package com.gymmanagement.controller;

import com.gymmanagement.model.Member;
import com.gymmanagement.service.MemberService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor

public class MemberController {

    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<List<Member>> getAllMembers(@RequestParam(required = false) String search) {
        if (search != null && !search.isEmpty()) {
            return ResponseEntity.ok(memberService.searchMembers(search));
        }
        return ResponseEntity.ok(memberService.getAllMembers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Member> getMemberById(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.getMemberById(id));
    }

    @PostMapping
    public ResponseEntity<Member> createMember(@RequestBody Member member) {
        return ResponseEntity.ok(memberService.createMember(member));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Member> updateMember(@PathVariable Long id, @RequestBody Member member) {
        return ResponseEntity.ok(memberService.updateMember(id, member));
    }

    @PutMapping("/{id}/weekly-workout")
    public ResponseEntity<Member> updateWeeklyWorkoutPlan(
            @PathVariable Long id,
            @Valid @RequestBody WeeklyWorkoutRequest request) {
        return ResponseEntity.ok(memberService.updateWeeklyWorkoutPlan(id, request.weeklyWorkoutPlan()));
    }

    @PostMapping("/{id}/renew")
    public ResponseEntity<Member> renewMembership(@PathVariable Long id, @RequestParam Long planId) {
        return ResponseEntity.ok(memberService.renewMembership(id, planId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }

    public record WeeklyWorkoutRequest(@NotBlank @Size(max = 10000) String weeklyWorkoutPlan) {
    }
}
