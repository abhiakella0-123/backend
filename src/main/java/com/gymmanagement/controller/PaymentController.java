package com.gymmanagement.controller;

import com.gymmanagement.model.Payment;
import com.gymmanagement.service.PaymentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor

public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping
    public ResponseEntity<List<Payment>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.getPaymentById(id));
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<Payment>> getPaymentsByMember(@PathVariable Long memberId) {
        return ResponseEntity.ok(paymentService.getPaymentsByMember(memberId));
    }

    @PostMapping("/member/{memberId}/checkout")
    public ResponseEntity<Payment> checkoutMembershipFee(
            @PathVariable Long memberId,
            @Valid @RequestBody CheckoutRequest request) {
        return ResponseEntity.ok(paymentService.checkoutMembershipFee(memberId, request.paymentMethod(), request.planId()));
    }

    @PostMapping
    public ResponseEntity<Payment> recordPayment(@RequestBody Payment payment) {
        return ResponseEntity.ok(paymentService.recordPayment(payment));
    }

    public record CheckoutRequest(
            @NotBlank @Pattern(regexp = "UPI|CREDIT_CARD|BANK_TRANSFER|CASH") String paymentMethod,
            Long planId) {
    }
}
