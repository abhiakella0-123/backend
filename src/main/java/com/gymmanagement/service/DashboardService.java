package com.gymmanagement.service;

import com.gymmanagement.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final MemberRepository memberRepository;
    private final TrainerRepository trainerRepository;
    private final GymClassRepository gymClassRepository;
    private final AttendanceRepository attendanceRepository;
    private final PaymentRepository paymentRepository;
    private final EquipmentRepository equipmentRepository;

    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();

        long totalMembers = memberRepository.count();
        long activeMembers = memberRepository.countByStatus("ACTIVE");
        long expiredMembers = memberRepository.countByStatus("EXPIRED");
        long totalTrainers = trainerRepository.count();
        long totalClasses = gymClassRepository.count();
        long todayCheckIns = attendanceRepository.countByDate(LocalDate.now());
        long activeInGym = attendanceRepository.countByDateAndStatus(LocalDate.now(), "CHECKED_IN");
        
        Double totalRevenue = paymentRepository.getTotalRevenue();
        LocalDateTime startOfMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        Double monthlyRevenue = paymentRepository.getRevenueSince(startOfMonth);

        long totalEquipment = equipmentRepository.count();
        long maintenanceNeeded = equipmentRepository.findByConditionStatus("MAINTENANCE_NEEDED").size() 
                + equipmentRepository.findByConditionStatus("BROKEN").size();

        stats.put("totalMembers", totalMembers);
        stats.put("activeMembers", activeMembers);
        stats.put("expiredMembers", expiredMembers);
        stats.put("totalTrainers", totalTrainers);
        stats.put("totalClasses", totalClasses);
        stats.put("todayCheckIns", todayCheckIns);
        stats.put("activeInGym", activeInGym);
        stats.put("totalRevenue", totalRevenue != null ? totalRevenue : 0.0);
        stats.put("monthlyRevenue", monthlyRevenue != null ? monthlyRevenue : 0.0);
        stats.put("totalEquipment", totalEquipment);
        stats.put("equipmentMaintenanceNeeded", maintenanceNeeded);

        return stats;
    }
}
