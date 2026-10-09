package com.gymmanagement.service;

import com.gymmanagement.model.Attendance;
import com.gymmanagement.model.Member;
import com.gymmanagement.repository.AttendanceRepository;
import com.gymmanagement.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final MemberRepository memberRepository;

    public List<Attendance> getTodayAttendance() {
        return attendanceRepository.findByDateOrderByCheckInTimeDesc(LocalDate.now());
    }

    public List<Attendance> getRecentAttendance() {
        return attendanceRepository.findTop20ByOrderByCheckInTimeDesc();
    }

    public List<Attendance> getMemberAttendance(Long memberId) {
        return attendanceRepository.findByMemberIdOrderByCheckInTimeDesc(memberId);
    }

    @Transactional
    public Attendance checkIn(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found with id: " + memberId));

        LocalDateTime now = LocalDateTime.now();
        boolean activeMembership = "ACTIVE".equalsIgnoreCase(member.getStatus());
        boolean activeTrial = "TRIAL".equalsIgnoreCase(member.getStatus())
                && member.getTrialEndsAt() != null
                && now.isBefore(member.getTrialEndsAt());
        if (!activeMembership && !activeTrial) {
            throw new RuntimeException("Membership or free trial is inactive. Pay membership fees to check in.");
        }

        LocalDate today = LocalDate.now();
        Optional<Attendance> active = attendanceRepository.findActiveCheckIn(memberId, today);
        if (active.isPresent()) {
            throw new RuntimeException("Member is already checked in for today.");
        }

        Attendance attendance = Attendance.builder()
                .member(member)
                .date(today)
                .checkInTime(now)
                .status("CHECKED_IN")
                .build();

        return attendanceRepository.save(attendance);
    }

    @Transactional
    public Attendance checkOut(Long memberId) {
        LocalDate today = LocalDate.now();
        Attendance attendance = attendanceRepository.findActiveCheckIn(memberId, today)
                .orElseThrow(() -> new RuntimeException("No active check-in found for member id: " + memberId));

        attendance.setCheckOutTime(LocalDateTime.now());
        attendance.setStatus("CHECKED_OUT");
        return attendanceRepository.save(attendance);
    }

    public long getActiveCheckInCount() {
        return attendanceRepository.countByDateAndStatus(LocalDate.now(), "CHECKED_IN");
    }

    public long getTodayTotalCheckIns() {
        return attendanceRepository.countByDate(LocalDate.now());
    }
}
