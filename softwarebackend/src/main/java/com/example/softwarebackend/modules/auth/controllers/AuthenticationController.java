package com.example.softwarebackend.modules.auth.controllers;

import com.example.softwarebackend.modules.auth.dto.request.*;
import com.example.softwarebackend.modules.auth.dto.response.AuthenticationResponseDTO;
import com.example.softwarebackend.modules.auth.services.AuthenticationService;
import com.example.softwarebackend.shared.dto.response.ApiResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("register")
    public ResponseEntity<ApiResponseDTO<AuthenticationResponseDTO>> registerUser(@Valid @RequestBody RegisterRequestDTO registerRequestDTO) {
        authenticationService.registerUser(registerRequestDTO);
        return ResponseEntity.ok(new ApiResponseDTO<>("200", "User registered successfully. Email Verification has been sent to your Email", null, true));
    }

    @PostMapping("login")
    public ResponseEntity<ApiResponseDTO<AuthenticationResponseDTO>> loginUser(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        AuthenticationResponseDTO response = authenticationService.loginUser(loginRequestDTO);
        return ResponseEntity.ok(new ApiResponseDTO<>("200", "User logged in successfully", response, true));
    }

    @PostMapping("verify-email")
    public ResponseEntity<ApiResponseDTO<String>> verifyEmail(@Valid @RequestBody VerifyEmailRequestDTO request) {
        authenticationService.verifyEmail(request.getEmail(), request.getOtpCode());
        return ResponseEntity.ok(new ApiResponseDTO<>("200", "Email verified successfully", null, true));
    }

    @PostMapping("send-otp")
    public ResponseEntity<ApiResponseDTO<String>> sendOtp(@Valid @RequestBody SendOtpRequestDTO request) {
        authenticationService.sendOtp(request.getEmail());
        return ResponseEntity.ok(new ApiResponseDTO<>("200", "OTP sent successfully", null, true));
    }

    @PostMapping("forgot-password")
    public ResponseEntity<ApiResponseDTO<String>> forgotPassword(@Valid @RequestBody SendOtpRequestDTO request) {
        authenticationService.sendOtp(request.getEmail());
        return ResponseEntity.ok(new ApiResponseDTO<>("200", "Password reset OTP sent to your email", null, true));
    }

    @PostMapping("reset-password")
    public ResponseEntity<ApiResponseDTO<String>> resetPassword(@Valid @RequestBody ResetPasswordRequestDTO request) {
        authenticationService.resetPassword(request);
        return ResponseEntity.ok(new ApiResponseDTO<>("200", "Password reset successfully", null, true));
    }
}
