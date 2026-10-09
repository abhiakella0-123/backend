package com.gymmanagement.service;

import com.gymmanagement.model.Member;
import com.gymmanagement.model.MembershipPlan;
import com.gymmanagement.repository.MemberRepository;
import com.gymmanagement.repository.MembershipPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class MemberAuthService {

    private static final String LEGACY_DEMO_PASSWORD = "member123";

    private final MemberRepository memberRepository;
    private final MembershipPlanRepository planRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional
    public Member register(String firstName, String lastName, String email, String phone,
                          String password, Long membershipPlanId) {
        String normalizedEmail = email.trim().toLowerCase(Locale.ROOT);
        if (memberRepository.existsByEmailIgnoreCase(normalizedEmail)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "An account with this email already exists.");
        }

        MembershipPlan plan = null;
        LocalDateTime now = LocalDateTime.now();
        LocalDate today = now.toLocalDate();
        if (membershipPlanId != null) {
            plan = planRepository.findById(membershipPlanId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Selected plan was not found."));
        }

        Member member = Member.builder()
                .firstName(firstName.trim())
                .lastName(lastName.trim())
                .email(normalizedEmail)
                .phone(phone == null || phone.isBlank() ? null : phone.trim())
                .passwordHash(passwordEncoder.encode(password))
                .joinDate(today)
                .membershipPlan(plan)
                .trialEndsAt(now.plusDays(2))
                .status("TRIAL")
                .build();

        return memberRepository.save(member);
    }

    @Transactional
    public Member login(String email, String password) {
        String normalizedEmail = email.trim().toLowerCase(Locale.ROOT);
        Member member = memberRepository.findByEmailIgnoreCase(normalizedEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password."));

        String passwordHash = member.getPasswordHash();
        if (passwordHash == null) {
            if (!LEGACY_DEMO_PASSWORD.equals(password)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password.");
            }
            member.setPasswordHash(passwordEncoder.encode(password));
            return memberRepository.save(member);
        }

        if (!passwordEncoder.matches(password, passwordHash)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password.");
        }
        return member;
    }
}