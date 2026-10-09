package com.gymmanagement.service;

import com.gymmanagement.model.Attendance;
import com.gymmanagement.model.Member;
import com.gymmanagement.model.Payment;
import com.gymmanagement.repository.AttendanceRepository;
import com.gymmanagement.repository.MemberRepository;
import com.gymmanagement.repository.MembershipPlanRepository;
import com.gymmanagement.repository.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MembershipPlanRepository planRepository;

    @Mock
    private AttendanceRepository attendanceRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private MemberService memberService;

    @Test
    void deleteMember_shouldDeleteAssociatedAttendanceAndPaymentsBeforeRemovingMember() {
        Long memberId = 1L;
        Member member = new Member();
        member.setId(memberId);

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(attendanceRepository.findByMemberIdOrderByCheckInTimeDesc(memberId)).thenReturn(List.of(new Attendance()));
        when(paymentRepository.findByMemberIdOrderByPaymentDateDesc(memberId)).thenReturn(List.of(new Payment()));

        memberService.deleteMember(memberId);

        verify(attendanceRepository).deleteAll(anyList());
        verify(paymentRepository).deleteAll(anyList());
        verify(memberRepository).delete(member);
    }

    @Test
    void updateWeeklyWorkoutPlan_shouldSavePlanOnMember() {
        Long memberId = 12L;
        String plan = "{\"weekStart\":\"2026-10-05\",\"sessions\":[]}";
        Member member = Member.builder().id(memberId).build();
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(memberRepository.save(member)).thenReturn(member);

        Member updated = memberService.updateWeeklyWorkoutPlan(memberId, plan);

        assertEquals(plan, updated.getWeeklyWorkoutPlan());
        verify(memberRepository).save(member);
    }
}
