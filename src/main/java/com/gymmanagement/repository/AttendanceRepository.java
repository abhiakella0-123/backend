package com.gymmanagement.repository;

import com.gymmanagement.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByDateOrderByCheckInTimeDesc(LocalDate date);

    List<Attendance> findByMemberIdOrderByCheckInTimeDesc(Long memberId);

    @Query("SELECT a FROM Attendance a WHERE a.member.id = :memberId AND a.date = :date AND a.status = 'CHECKED_IN'")
    Optional<Attendance> findActiveCheckIn(Long memberId, LocalDate date);

    long countByDate(LocalDate date);

    long countByDateAndStatus(LocalDate date, String status);

    List<Attendance> findTop20ByOrderByCheckInTimeDesc();
}
