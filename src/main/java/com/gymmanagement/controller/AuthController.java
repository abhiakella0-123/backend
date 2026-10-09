package com.gymmanagement.controller;

import com.gymmanagement.model.Member;
import com.gymmanagement.service.MemberAuthService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final MemberAuthService memberAuthService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        Member member = memberAuthService.register(
                request.firstName(), request.lastName(), request.email(), request.phone(),
                request.password(), request.membershipPlanId());
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(member));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(toResponse(memberAuthService.login(request.email(), request.password())));
    }

    private AuthResponse toResponse(Member member) {
        return new AuthResponse("user", member.getId(), member.getEmail());
    }

    public record RegisterRequest(
            @NotBlank @Size(max = 100) String firstName,
            @NotBlank @Size(max = 100) String lastName,
            @NotBlank @Email @Size(max = 150) String email,
            @NotBlank @Size(min = 8, max = 72) String password,
            @Size(max = 20) String phone,
            Long membershipPlanId) {
    }

    public record LoginRequest(
            @NotBlank @Email @Size(max = 150) String email,
            @NotBlank String password) {
    }

    public record AuthResponse(String role, Long memberId, String email) {
    }
}