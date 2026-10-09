package com.gymmanagement.service;

import com.gymmanagement.model.Attendance;
import com.gymmanagement.model.Member;
import com.gymmanagement.repository.AttendanceRepository;
import com.gymmanagement.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AttendanceServiceTest {

    @Mock
    private AttendanceRepository attendanceRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private AttendanceService attendanceService;

    @Test
    void checkIn_allowsMemberDuringFreeTrial() {
        Member member = Member.builder().id(5L).status("TRIAL")
                .trialEndsAt(LocalDateTime.now().plusHours(1)).build();
        when(memberRepository.findById(5L)).thenReturn(Optional.of(member));
        when(attendanceRepository.findActiveCheckIn(5L, LocalDate.now())).thenReturn(Optional.empty());
        when(attendanceRepository.save(any(Attendance.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Attendance attendance = attendanceService.checkIn(5L);

        assertEquals("CHECKED_IN", attendance.getStatus());
    }

    @Test
    void checkIn_rejectsExpiredTrial() {
        Member member = Member.builder().id(5L).status("TRIAL")
                .trialEndsAt(LocalDateTime.now().minusSeconds(1)).build();
        when(memberRepository.findById(5L)).thenReturn(Optional.of(member));

        assertThrows(RuntimeException.class, () -> attendanceService.checkIn(5L));
        verify(attendanceRepository, never()).save(any(Attendance.class));
    }
}