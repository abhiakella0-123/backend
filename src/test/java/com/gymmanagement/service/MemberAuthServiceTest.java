package com.gymmanagement.service;

import com.gymmanagement.model.Member;
import com.gymmanagement.repository.MemberRepository;
import com.gymmanagement.repository.MembershipPlanRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberAuthServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MembershipPlanRepository planRepository;

    @InjectMocks
    private MemberAuthService memberAuthService;

    @Test
    void register_startsNewMemberOnFortyEightHourTrial() {
        when(memberRepository.existsByEmailIgnoreCase("new.member@example.com")).thenReturn(false);
        when(memberRepository.save(any(Member.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Member member = memberAuthService.register(
                "New", "Member", "new.member@example.com", null, "password123", null);

        assertEquals("TRIAL", member.getStatus());
        assertTrue(member.getTrialEndsAt().isAfter(LocalDateTime.now().plusHours(47)));
        assertTrue(member.getTrialEndsAt().isBefore(LocalDateTime.now().plusHours(49)));
    }
}