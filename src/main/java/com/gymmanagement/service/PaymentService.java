package com.gymmanagement.service;

import com.gymmanagement.model.Member;
import com.gymmanagement.model.MembershipPlan;
import com.gymmanagement.model.Payment;
import com.gymmanagement.repository.MemberRepository;
import com.gymmanagement.repository.MembershipPlanRepository;
import com.gymmanagement.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final MemberRepository memberRepository;
    private final MembershipPlanRepository planRepository;

    public List<Payment> getAllPayments() {
        return paymentRepository.findAllByOrderByPaymentDateDesc();
    }

    public List<Payment> getPaymentsByMember(Long memberId) {
        return paymentRepository.findByMemberIdOrderByPaymentDateDesc(memberId);
    }

    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found with id: " + id));
    }

        private void rejectDuplicatePaidFee(Member member, MembershipPlan plan) {
        if (plan == null || plan.getId() == null) return;
        if (member.getMembershipEndDate() != null && member.getMembershipEndDate().isBefore(LocalDate.now())) return;

        LocalDateTime membershipStart = member.getMembershipStartDate() == null
            ? LocalDateTime.MIN
            : member.getMembershipStartDate().atStartOfDay();
        List<Payment> memberPayments = paymentRepository.findByMemberIdOrderByPaymentDateDesc(member.getId());
        boolean alreadyPaid = memberPayments != null && memberPayments.stream().anyMatch(existing ->
            "PAID".equalsIgnoreCase(existing.getStatus())
                && existing.getMembershipPlan() != null
                && plan.getId().equals(existing.getMembershipPlan().getId())
                && !existing.getPaymentDate().isBefore(membershipStart));
        if (alreadyPaid) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "This member has already paid for the selected plan during the current membership period.");
        }
        }

    @Transactional
    public Payment checkoutMembershipFee(Long memberId, String paymentMethod, Long planId) {
        Set<String> acceptedMethods = Set.of("UPI", "CREDIT_CARD", "BANK_TRANSFER", "CASH");
        if (!acceptedMethods.contains(paymentMethod)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Choose a supported payment method.");
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Member not found."));
        MembershipPlan plan = planId == null
                ? member.getMembershipPlan()
                : planRepository.findById(planId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Membership plan not found."));
        if (plan == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A membership plan is required to pay fees.");
        }
        if (!Boolean.TRUE.equals(plan.getActive())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This membership plan is no longer available.");
        }
        rejectDuplicatePaidFee(member, plan);

        LocalDate today = LocalDate.now();
        member.setMembershipPlan(plan);
        member.setMembershipStartDate(today);
        member.setMembershipEndDate(today.plusMonths(plan.getDurationMonths()));
        member.setTrialEndsAt(null);
        member.setStatus("ACTIVE");
        memberRepository.save(member);

        Payment payment = Payment.builder()
                .member(member)
                .membershipPlan(plan)
                .amount(plan.getPrice())
                .paymentDate(LocalDateTime.now())
                .paymentMethod(paymentMethod)
                .status("PAID")
                .notes("Member portal membership fee")
                .build();
        return recordPayment(payment);
    }

    @Transactional
    public Payment recordPayment(Payment payment) {
        if (payment.getMember() == null || payment.getMember().getId() == null) {
            throw new RuntimeException("Member ID is required for payment.");
        }
        Member member = memberRepository.findById(payment.getMember().getId())
                .orElseThrow(() -> new RuntimeException("Member not found with id: " + payment.getMember().getId()));
        payment.setMember(member);

        if (payment.getMembershipPlan() != null && payment.getMembershipPlan().getId() != null) {
            MembershipPlan plan = planRepository.findById(payment.getMembershipPlan().getId()).orElse(null);
            payment.setMembershipPlan(plan);
        } else if (member.getMembershipPlan() != null) {
            payment.setMembershipPlan(member.getMembershipPlan());
        }

        String paymentStatus = payment.getStatus() == null ? "PAID" : payment.getStatus();
        if ("PAID".equalsIgnoreCase(paymentStatus)) {
            rejectDuplicatePaidFee(member, payment.getMembershipPlan());
        }
        if (payment.getMembershipPlan() != null) {
            payment.setAmount(payment.getMembershipPlan().getPrice());
        }

        if (payment.getInvoiceNumber() == null || payment.getInvoiceNumber().isEmpty()) {
            payment.setInvoiceNumber("INV-" + System.currentTimeMillis() % 1000000 + "-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase());
        }

        if (payment.getPaymentDate() == null) {
            payment.setPaymentDate(LocalDateTime.now());
        }

        payment.setStatus(paymentStatus);

        return paymentRepository.save(payment);
    }
}
