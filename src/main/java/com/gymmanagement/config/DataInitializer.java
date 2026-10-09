package com.gymmanagement.config;

import com.gymmanagement.model.*;
import com.gymmanagement.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final MembershipPlanRepository planRepository;
    private final TrainerRepository trainerRepository;
    private final MemberRepository memberRepository;
    private final GymClassRepository classRepository;
    private final EquipmentRepository equipmentRepository;
    private final AttendanceRepository attendanceRepository;
    private final PaymentRepository paymentRepository;

    @Override
    public void run(String... args) throws Exception {
        if (planRepository.count() > 0) {
            log.info("Database already seeded. Skipping initial data population.");
            return;
        }

        log.info("Seeding realistic sample data into Gym Management database...");

        // 1. Membership Plans
        MembershipPlan bronze = planRepository.save(MembershipPlan.builder()
                .name("Bronze Basic")
                .description("Access to general gym floor & standard cardio/weight machines during non-peak hours.")
                .durationMonths(1)
                .price(2999.0)
                .features("Gym Floor Access, Locker Room Access, 1 Fitness Assessment")
                .active(true)
                .build());

        MembershipPlan silver = planRepository.save(MembershipPlan.builder()
                .name("Silver Pro")
                .description("All-day unlimited access, group fitness classes, and free sauna.")
                .durationMonths(3)
                .price(7999.0)
                .features("All-Day Access, Unlimited Group Classes, Sauna & Steam, 2 Guest Passes/mo")
                .active(true)
                .build());

        MembershipPlan gold = planRepository.save(MembershipPlan.builder()
                .name("Gold Elite")
                .description("Full premium access with personal training sessions, nutrition planning, and VIP lounge.")
                .durationMonths(6)
                .price(14999.0)
                .features("All Silver Features, 4 Personal Training Sessions/mo, Nutrition Guide, VIP Locker Room")
                .active(true)
                .build());

        MembershipPlan platinum = planRepository.save(MembershipPlan.builder()
                .name("Platinum Annual VIP")
                .description("Ultimate 12-month membership with full access, unlimited trainer consultations, and gym gear kit.")
                .durationMonths(12)
                .price(26999.0)
                .features("All Gold Features, Unlimited Trainer Guidance, Welcome Gear Pack, Free Towel Service, Priority Class Booking")
                .active(true)
                .build());

        // 2. Trainers
        Trainer t1 = trainerRepository.save(Trainer.builder()
                .firstName("Marcus")
                .lastName("Vance")
                .email("marcus.vance@gymelite.com")
                .phone("+91 98765 43210")
                .specialization("Strength & Bodybuilding")
                .experienceYears(8)
                .rating(4.9)
                .salary(520000.0)
                .imageUrl("https://images.unsplash.com/photo-1567013127542-490d757e51fc?w=300&auto=format&fit=crop&q=80")
                .status("ACTIVE")
                .build());

        Trainer t2 = trainerRepository.save(Trainer.builder()
                .firstName("Elena")
                .lastName("Rostova")
                .email("elena.rostova@gymelite.com")
                .phone("+91 91234 56789")
                .specialization("HIIT & Functional Cardio")
                .experienceYears(5)
                .rating(4.8)
                .salary(460000.0)
                .imageUrl("https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?w=300&auto=format&fit=crop&q=80")
                .status("ACTIVE")
                .build());

        Trainer t3 = trainerRepository.save(Trainer.builder()
                .firstName("Devon")
                .lastName("Brooks")
                .email("devon.brooks@gymelite.com")
                .phone("+91 99876 54321")
                .specialization("CrossFit & Calisthenics")
                .experienceYears(6)
                .rating(4.9)
                .salary(480000.0)
                .imageUrl("https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=300&auto=format&fit=crop&q=80")
                .status("ACTIVE")
                .build());

        Trainer t4 = trainerRepository.save(Trainer.builder()
                .firstName("Aria")
                .lastName("Chen")
                .email("aria.chen@gymelite.com")
                .phone("+91 98765 12345")
                .specialization("Yoga, Pilates & Mobility")
                .experienceYears(7)
                .rating(5.0)
                .salary(490000.0)
                .imageUrl("https://images.unsplash.com/photo-1517841905240-472988babdf9?w=300&auto=format&fit=crop&q=80")
                .status("ACTIVE")
                .build());

        // 3. Members
        Member m1 = memberRepository.save(Member.builder()
                .firstName("Alex")
                .lastName("Mercer")
                .email("alex.mercer@gmail.com")
                .phone("+91 98765 11220")
                .gender("Male")
                .dateOfBirth(LocalDate.of(1995, 3, 14))
                .joinDate(LocalDate.now().minusMonths(4))
                .membershipPlan(gold)
                .membershipStartDate(LocalDate.now().minusMonths(4))
                .membershipEndDate(LocalDate.now().plusMonths(2))
                .status("ACTIVE")
                .emergencyContact("Sarah Mercer (+91 98765 11221)")
                .notes("Focusing on chest hypertrophy and deadlift form.")
                .avatarUrl("https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=150&auto=format&fit=crop&q=80")
                .build());

        Member m2 = memberRepository.save(Member.builder()
                .firstName("Sophia")
                .lastName("Taylor")
                .email("sophia.t@outlook.com")
                .phone("+91 98765 22330")
                .gender("Female")
                .dateOfBirth(LocalDate.of(1998, 7, 22))
                .joinDate(LocalDate.now().minusMonths(2))
                .membershipPlan(platinum)
                .membershipStartDate(LocalDate.now().minusMonths(2))
                .membershipEndDate(LocalDate.now().plusMonths(10))
                .status("ACTIVE")
                .emergencyContact("David Taylor (+91 98765 22331)")
                .notes("Preparing for half marathon in November.")
                .avatarUrl("https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=150&auto=format&fit=crop&q=80")
                .build());

        memberRepository.save(Member.builder()
                .firstName("Jordan")
                .lastName("Smith")
                .email("jordan.smith@yahoo.com")
                .phone("+91 98765 33440")
                .gender("Non-Binary")
                .dateOfBirth(LocalDate.of(2000, 11, 5))
                .joinDate(LocalDate.now().minusMonths(6))
                .membershipPlan(silver)
                .membershipStartDate(LocalDate.now().minusMonths(6))
                .membershipEndDate(LocalDate.now().minusMonths(3))
                .status("EXPIRED")
                .emergencyContact("Lisa Smith (+91 98765 33441)")
                .notes("Follow-up required for membership renewal.")
                .avatarUrl("https://images.unsplash.com/photo-1570295999919-56ceb5ecca61?w=150&auto=format&fit=crop&q=80")
                .build());

        Member m4 = memberRepository.save(Member.builder()
                .firstName("Liam")
                .lastName("Davies")
                .email("liam.davies@gmail.com")
                .phone("+91 98765 44550")
                .gender("Male")
                .dateOfBirth(LocalDate.of(1992, 1, 19))
                .joinDate(LocalDate.now().minusMonths(1))
                .membershipPlan(gold)
                .membershipStartDate(LocalDate.now().minusMonths(1))
                .membershipEndDate(LocalDate.now().plusMonths(5))
                .status("ACTIVE")
                .emergencyContact("Emma Davies (+91 98765 44551)")
                .notes("Attends 6:30 AM functional fitness classes regularly.")
                .avatarUrl("https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150&auto=format&fit=crop&q=80")
                .build());

        Member m5 = memberRepository.save(Member.builder()
                .firstName("Maya")
                .lastName("Patel")
                .email("maya.patel@gmail.com")
                .phone("+91 98765 55660")
                .gender("Female")
                .dateOfBirth(LocalDate.of(1996, 9, 30))
                .joinDate(LocalDate.now().minusDays(10))
                .membershipPlan(bronze)
                .membershipStartDate(LocalDate.now().minusDays(10))
                .membershipEndDate(LocalDate.now().plusDays(20))
                .status("ACTIVE")
                .emergencyContact("Raj Patel (+91 98765 55661)")
                .notes("Interested in yoga and core conditioning.")
                .avatarUrl("https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=150&auto=format&fit=crop&q=80")
                .build());

        // 4. Gym Classes
        classRepository.save(GymClass.builder()
                .className("Iron Core & Powerlifting")
                .description("High intensity compound lifts focusing on squats, deadlifts, and bench technique.")
                .trainer(t1)
                .scheduleTime("06:30 AM - 07:30 AM")
                .daysOfWeek("Mon, Wed, Fri")
                .durationMinutes(60)
                .capacity(15)
                .enrolledCount(12)
                .room("Main Weight Floor")
                .build());

        classRepository.save(GymClass.builder()
                .className("Metabolic HIIT Burn")
                .description("Explosive interval circuits designed to maximize calorie burn and metabolic rate.")
                .trainer(t2)
                .scheduleTime("08:00 AM - 08:45 AM")
                .daysOfWeek("Tue, Thu, Sat")
                .durationMinutes(45)
                .capacity(20)
                .enrolledCount(18)
                .room("Cardio Arena")
                .build());

        classRepository.save(GymClass.builder()
                .className("CrossFit Synergy")
                .description("WOD workouts combining Olympic lifting, gymnastics, and kettlebell intervals.")
                .trainer(t3)
                .scheduleTime("05:30 PM - 06:30 PM")
                .daysOfWeek("Mon, Wed, Fri")
                .durationMinutes(60)
                .capacity(18)
                .enrolledCount(15)
                .room("CrossFit Box B")
                .build());

        classRepository.save(GymClass.builder()
                .className("Sunrise Vinyasa Flow")
                .description("Restorative dynamic yoga flow for posture alignment, flexibility, and core stability.")
                .trainer(t4)
                .scheduleTime("07:00 AM - 08:00 AM")
                .daysOfWeek("Tue, Thu, Sun")
                .durationMinutes(60)
                .capacity(25)
                .enrolledCount(21)
                .room("Zen Yoga Studio")
                .build());

        // 5. Equipment
        equipmentRepository.saveAll(Arrays.asList(
                Equipment.builder().name("LifeFitness Treadmill T5 Pro").category("CARDIO").quantity(8).conditionStatus("GOOD").location("Cardio Deck Zone 1").lastMaintenanceDate(LocalDate.now().minusWeeks(3)).purchaseDate(LocalDate.now().minusYears(2)).build(),
                Equipment.builder().name("Concept2 RowErg Model D").category("CARDIO").quantity(5).conditionStatus("GOOD").location("Cardio Deck Zone 2").lastMaintenanceDate(LocalDate.now().minusWeeks(1)).purchaseDate(LocalDate.now().minusYears(1)).build(),
                Equipment.builder().name("Olympic Power Rack with Pull-up Bar").category("STRENGTH").quantity(4).conditionStatus("GOOD").location("Strength Bay A").lastMaintenanceDate(LocalDate.now().minusWeeks(5)).purchaseDate(LocalDate.now().minusYears(3)).build(),
                Equipment.builder().name("Hammer Strength Leg Press 45-deg").category("STRENGTH").quantity(2).conditionStatus("MAINTENANCE_NEEDED").location("Strength Bay B").lastMaintenanceDate(LocalDate.now().minusMonths(2)).purchaseDate(LocalDate.now().minusYears(2)).build(),
                Equipment.builder().name("Urethane Dumbbell Set (5 lbs - 120 lbs)").category("FREE_WEIGHTS").quantity(2).conditionStatus("GOOD").location("Free Weight Area").lastMaintenanceDate(LocalDate.now().minusMonths(1)).purchaseDate(LocalDate.now().minusYears(1)).build(),
                Equipment.builder().name("Dual Adjustable Pulley Cable Crossover").category("STRENGTH").quantity(2).conditionStatus("GOOD").location("Center Floor").lastMaintenanceDate(LocalDate.now().minusWeeks(2)).purchaseDate(LocalDate.now().minusYears(2)).build(),
                Equipment.builder().name("Assault Fitness AirBike Pro").category("CARDIO").quantity(4).conditionStatus("GOOD").location("HIIT Zone").lastMaintenanceDate(LocalDate.now().minusWeeks(4)).purchaseDate(LocalDate.now().minusMonths(8)).build(),
                Equipment.builder().name("Hyperice Normatec Compression Boots").category("RECOVERY").quantity(3).conditionStatus("GOOD").location("Recovery Lounge").lastMaintenanceDate(LocalDate.now().minusDays(10)).purchaseDate(LocalDate.now().minusMonths(6)).build()
        ));

        // 6. Attendance (Today & Recent)
        attendanceRepository.save(Attendance.builder()
                .member(m1)
                .date(LocalDate.now())
                .checkInTime(LocalDateTime.now().minusHours(1).minusMinutes(20))
                .status("CHECKED_IN")
                .build());

        attendanceRepository.save(Attendance.builder()
                .member(m2)
                .date(LocalDate.now())
                .checkInTime(LocalDateTime.now().minusHours(2).minusMinutes(10))
                .checkOutTime(LocalDateTime.now().minusMinutes(25))
                .status("CHECKED_OUT")
                .build());

        attendanceRepository.save(Attendance.builder()
                .member(m4)
                .date(LocalDate.now())
                .checkInTime(LocalDateTime.now().minusMinutes(40))
                .status("CHECKED_IN")
                .build());

        // 7. Payments & Invoices
        paymentRepository.save(Payment.builder()
                .invoiceNumber("INV-2026-00101")
                .member(m1)
                .membershipPlan(gold)
                .amount(gold.getPrice())
                .paymentDate(LocalDateTime.now().minusMonths(4))
                .paymentMethod("CREDIT_CARD")
                .status("PAID")
                .notes("6-month Gold Elite membership renewal")
                .build());

        paymentRepository.save(Payment.builder()
                .invoiceNumber("INV-2026-00102")
                .member(m2)
                .membershipPlan(platinum)
                .amount(platinum.getPrice())
                .paymentDate(LocalDateTime.now().minusMonths(2))
                .paymentMethod("UPI")
                .status("PAID")
                .notes("Annual VIP pass upfront payment")
                .build());

        paymentRepository.save(Payment.builder()
                .invoiceNumber("INV-2026-00103")
                .member(m4)
                .membershipPlan(gold)
                .amount(gold.getPrice())
                .paymentDate(LocalDateTime.now().minusMonths(1))
                .paymentMethod("CREDIT_CARD")
                .status("PAID")
                .notes("Semester fitness membership")
                .build());

        paymentRepository.save(Payment.builder()
                .invoiceNumber("INV-2026-00104")
                .member(m5)
                .membershipPlan(bronze)
                .amount(bronze.getPrice())
                .paymentDate(LocalDateTime.now().minusDays(10))
                .paymentMethod("CASH")
                .status("PAID")
                .notes("Monthly starter membership")
                .build());

        log.info("Sample gym data successfully populated!");
    }
}
