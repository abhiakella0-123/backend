package com.gymmanagement.service;

import com.gymmanagement.model.Member;
import com.gymmanagement.model.MembershipPlan;
import com.gymmanagement.model.Payment;
import com.gymmanagement.repository.MemberRepository;
import com.gymmanagement.repository.MembershipPlanRepository;
import com.gymmanagement.repository.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

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
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MembershipPlanRepository planRepository;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    void checkoutMembershipFee_usesAssignedPlanPriceAndSavesAsPaid() {
        MembershipPlan plan = MembershipPlan.builder().id(4L).price(1499.0).durationMonths(1).build();
        Member member = Member.builder().id(8L).membershipPlan(plan).status("TRIAL")
            .trialEndsAt(LocalDateTime.now().plusDays(2)).build();
        when(memberRepository.findById(8L)).thenReturn(Optional.of(member));
        when(planRepository.findById(4L)).thenReturn(Optional.of(plan));
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Payment payment = paymentService.checkoutMembershipFee(8L, "UPI", null);

        assertEquals(1499.0, payment.getAmount());
        assertEquals("PAID", payment.getStatus());
        assertEquals("UPI", payment.getPaymentMethod());
        assertEquals(plan, payment.getMembershipPlan());
        assertEquals("ACTIVE", member.getStatus());
        assertEquals(null, member.getTrialEndsAt());
        assertEquals(LocalDate.now(), member.getMembershipStartDate());
        verify(paymentRepository).save(any(Payment.class));
    }

    @Test
    void checkoutMembershipFee_acceptsCash() {
        MembershipPlan plan = MembershipPlan.builder().id(4L).price(1499.0).durationMonths(1).build();
        Member member = Member.builder().id(8L).membershipPlan(plan).build();
        when(memberRepository.findById(8L)).thenReturn(Optional.of(member));
        when(planRepository.findById(4L)).thenReturn(Optional.of(plan));
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Payment payment = paymentService.checkoutMembershipFee(8L, "CASH", null);

        assertEquals("CASH", payment.getPaymentMethod());
        assertEquals("PAID", payment.getStatus());
    }

    @Test
    void checkoutMembershipFee_assignsSelectedPlanWhenMemberHasNone() {
        MembershipPlan plan = MembershipPlan.builder().id(4L).price(1499.0).durationMonths(1).build();
        Member member = Member.builder().id(8L).status("TRIAL").build();
        when(memberRepository.findById(8L)).thenReturn(Optional.of(member));
        when(planRepository.findById(4L)).thenReturn(Optional.of(plan));
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Payment payment = paymentService.checkoutMembershipFee(8L, "UPI", 4L);

        assertEquals(plan, member.getMembershipPlan());
        assertEquals(plan, payment.getMembershipPlan());
        assertEquals("ACTIVE", member.getStatus());
    }

    @Test
    void checkoutMembershipFee_rejectsDuplicatePaymentForCurrentMembership() {
        MembershipPlan plan = MembershipPlan.builder().id(4L).price(1499.0).durationMonths(1).build();
        Member member = Member.builder().id(8L).membershipPlan(plan).status("ACTIVE")
                .membershipStartDate(LocalDate.now().minusDays(2))
                .membershipEndDate(LocalDate.now().plusDays(28)).build();
        Payment existingPayment = Payment.builder().member(member).membershipPlan(plan)
                .amount(1499.0).status("PAID").paymentMethod("UPI")
                .paymentDate(LocalDateTime.now().minusDays(1)).build();
        when(memberRepository.findById(8L)).thenReturn(Optional.of(member));
        when(paymentRepository.findByMemberIdOrderByPaymentDateDesc(8L)).thenReturn(java.util.List.of(existingPayment));

        assertThrows(ResponseStatusException.class, () -> paymentService.checkoutMembershipFee(8L, "UPI", null));
        verify(paymentRepository, never()).save(any(Payment.class));
    }

    @Test
    void recordPayment_rejectsAdminDuplicateForCurrentMembership() {
        MembershipPlan plan = MembershipPlan.builder().id(4L).price(1499.0).durationMonths(1).build();
        Member member = Member.builder().id(8L).membershipPlan(plan).status("ACTIVE")
                .membershipStartDate(LocalDate.now().minusDays(2))
                .membershipEndDate(LocalDate.now().plusDays(28)).build();
        Payment existingPayment = Payment.builder().member(member).membershipPlan(plan)
                .amount(1499.0).status("PAID").paymentMethod("UPI")
                .paymentDate(LocalDateTime.now().minusDays(1)).build();
        Payment attemptedPayment = Payment.builder().member(member).membershipPlan(plan)
                .amount(1499.0).paymentMethod("CASH").status("PAID").build();
        when(memberRepository.findById(8L)).thenReturn(Optional.of(member));
        when(planRepository.findById(4L)).thenReturn(Optional.of(plan));
        when(paymentRepository.findByMemberIdOrderByPaymentDateDesc(8L)).thenReturn(java.util.List.of(existingPayment));

        assertThrows(ResponseStatusException.class, () -> paymentService.recordPayment(attemptedPayment));
        verify(paymentRepository, never()).save(any(Payment.class));
    }
}