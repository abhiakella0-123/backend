package com.gymmanagement.controller;

import com.gymmanagement.model.Attendance;
import com.gymmanagement.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor

public class AttendanceController {

    private final AttendanceService attendanceService;

    @GetMapping("/today")
    public ResponseEntity<List<Attendance>> getTodayAttendance() {
        return ResponseEntity.ok(attendanceService.getTodayAttendance());
    }

    @GetMapping("/recent")
    public ResponseEntity<List<Attendance>> getRecentAttendance() {
        return ResponseEntity.ok(attendanceService.getRecentAttendance());
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<Attendance>> getMemberAttendance(@PathVariable Long memberId) {
        return ResponseEntity.ok(attendanceService.getMemberAttendance(memberId));
    }

    @PostMapping("/check-in/{memberId}")
    public ResponseEntity<Attendance> checkIn(@PathVariable Long memberId) {
        return ResponseEntity.ok(attendanceService.checkIn(memberId));
    }

    @PostMapping("/check-out/{memberId}")
    public ResponseEntity<Attendance> checkOut(@PathVariable Long memberId) {
        return ResponseEntity.ok(attendanceService.checkOut(memberId));
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> getAttendanceStats() {
        return ResponseEntity.ok(Map.of(
                "activeInside", attendanceService.getActiveCheckInCount(),
                "todayTotal", attendanceService.getTodayTotalCheckIns()
        ));
    }
}
