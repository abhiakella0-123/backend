package com.gymmanagement.service;

import com.gymmanagement.model.Attendance;
import com.gymmanagement.model.Member;
import com.gymmanagement.model.MembershipPlan;
import com.gymmanagement.model.Payment;
import com.gymmanagement.repository.AttendanceRepository;
import com.gymmanagement.repository.MemberRepository;
import com.gymmanagement.repository.MembershipPlanRepository;
import com.gymmanagement.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MembershipPlanRepository planRepository;
    private final AttendanceRepository attendanceRepository;
    private final PaymentRepository paymentRepository;

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    public List<Member> searchMembers(String query) {
        if (query == null || query.trim().isEmpty()) {
            return memberRepository.findAll();
        }
        return memberRepository.searchMembers(query.trim());
    }

    public Member getMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found with id: " + id));
    }

    @Transactional
    public Member createMember(Member member) {
        if (member.getJoinDate() == null) {
            member.setJoinDate(LocalDate.now());
        }
        if (member.getMembershipPlan() != null && member.getMembershipPlan().getId() != null) {
            MembershipPlan plan = planRepository.findById(member.getMembershipPlan().getId())
                    .orElse(null);
            member.setMembershipPlan(plan);
            if (plan != null && member.getMembershipStartDate() == null) {
                member.setMembershipStartDate(LocalDate.now());
                member.setMembershipEndDate(LocalDate.now().plusMonths(plan.getDurationMonths()));
                member.setStatus("ACTIVE");
            }
        }
        if (member.getStatus() == null) {
            member.setStatus("ACTIVE");
        }
        return memberRepository.save(member);
    }

    @Transactional
    public Member updateMember(Long id, Member details) {
        Member member = getMemberById(id);
        member.setFirstName(details.getFirstName());
        member.setLastName(details.getLastName());
        member.setEmail(details.getEmail());
        member.setPhone(details.getPhone());
        member.setGender(details.getGender());
        member.setDateOfBirth(details.getDateOfBirth());
        member.setEmergencyContact(details.getEmergencyContact());
        member.setNotes(details.getNotes());
        member.setStatus(details.getStatus());

        if (details.getMembershipPlan() != null && details.getMembershipPlan().getId() != null) {
            MembershipPlan plan = planRepository.findById(details.getMembershipPlan().getId()).orElse(null);
            member.setMembershipPlan(plan);
        }

        if (details.getMembershipStartDate() != null) {
            member.setMembershipStartDate(details.getMembershipStartDate());
        }
        if (details.getMembershipEndDate() != null) {
            member.setMembershipEndDate(details.getMembershipEndDate());
        }
        if (details.getAvatarUrl() != null) {
            member.setAvatarUrl(details.getAvatarUrl());
        }

        return memberRepository.save(member);
    }

    @Transactional
    public Member updateWeeklyWorkoutPlan(Long id, String weeklyWorkoutPlan) {
        Member member = getMemberById(id);
        member.setWeeklyWorkoutPlan(weeklyWorkoutPlan);
        return memberRepository.save(member);
    }

    @Transactional
    public Member renewMembership(Long id, Long planId) {
        Member member = getMemberById(id);
        MembershipPlan plan = planRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan not found with id: " + planId));
        
        member.setMembershipPlan(plan);
        LocalDate start = LocalDate.now();
        if (member.getMembershipEndDate() != null && member.getMembershipEndDate().isAfter(LocalDate.now())) {
            start = member.getMembershipEndDate();
        }
        member.setMembershipStartDate(start);
        member.setMembershipEndDate(start.plusMonths(plan.getDurationMonths()));
        member.setStatus("ACTIVE");
        return memberRepository.save(member);
    }

    @Transactional
    public void deleteMember(Long id) {
        Member member = getMemberById(id);

        List<Attendance> attendanceRecords = attendanceRepository.findByMemberIdOrderByCheckInTimeDesc(id);
        if (!attendanceRecords.isEmpty()) {
            attendanceRepository.deleteAll(attendanceRecords);
        }

        List<Payment> payments = paymentRepository.findByMemberIdOrderByPaymentDateDesc(id);
        if (!payments.isEmpty()) {
            paymentRepository.deleteAll(payments);
        }

        memberRepository.delete(member);
    }
}
