package com.example.softwarebackend.modules.auth.services;

import com.example.softwarebackend.modules.auth.dto.request.LoginRequestDTO;
import com.example.softwarebackend.modules.auth.dto.request.RegisterRequestDTO;
import com.example.softwarebackend.modules.auth.dto.request.ResetPasswordRequestDTO;
import com.example.softwarebackend.modules.auth.dto.response.AuthenticationResponseDTO;
import com.example.softwarebackend.modules.auth.mappers.AuthenticationMapper;
import com.example.softwarebackend.modules.otp.services.OtpService;
import com.example.softwarebackend.modules.user.services.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final OtpService otpService;

    @Value("${app.account.lock.duration.minutes}")
    private int lockTimeDurationInMinutes;

    @Value("${app.account.lock.max-failed-attempts}")
    private int maximumFailedAttempts;

    public void registerUser(RegisterRequestDTO registerRequestDTO) {
        if (userService.getUserEntityByEmail(registerRequestDTO.getEmail()) != null) {
            throw new IllegalArgumentException("Registration failed. Please try again with different details");
        }

        var user = AuthenticationMapper.toUserFromRegisterRequestDTO(registerRequestDTO, passwordEncoder);
        var createdUser = userService.saveUserEntity(user);
        logger.info("User Created with email: {}", createdUser.getEmail());

        otpService.sendOtp(createdUser);
    }

    public AuthenticationResponseDTO loginUser(LoginRequestDTO loginRequestDTO) {
        var user = userService.getUserEntityByEmail(loginRequestDTO.getEmail());

        if (user.isAccountLocked() && user.getLockTime() != null) {
            if (user.getLockTime().plusMinutes(lockTimeDurationInMinutes).isAfter(LocalDateTime.now())) {
                throw new IllegalArgumentException("Account is locked. Try again later.");
            } else {
                userService.updateAccountLockDetails(user.getId(), false, null, 0);
                user.setAccountLocked(false);
                user.setFailedLoginAttempts(0);
            }
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword())
            );

            if (user.getFailedLoginAttempts() > 0) {
                userService.updateAccountLockDetails(user.getId(), false, null, 0);
            }

            userService.updateLastLoginTime(user.getId());

            var jwtToken = jwtService.generateToken(user);
            var userSummary = AuthenticationMapper.toUserSummaryDTO(user);

            return AuthenticationResponseDTO.builder()
                    .token(jwtToken)
                    .user(userSummary)
                    .build();

        } catch (Exception e) {
            int failedAttempts = user.getFailedLoginAttempts() + 1;
            if (failedAttempts >= maximumFailedAttempts) {
                userService.updateAccountLockDetails(user.getId(), true, LocalDateTime.now(), failedAttempts);
                throw new IllegalArgumentException("Account locked due to too many failed login attempts");
            } else {
                userService.updateAccountLockDetails(user.getId(), false, null, failedAttempts);
                throw new IllegalArgumentException("Invalid credentials");
            }
        }
    }

    public void verifyEmail(String email, String otpCode) {
        var user = userService.getUserEntityByEmail(email);

        if (!otpService.verifyOTP(user, otpCode)) {
            throw new IllegalArgumentException("Invalid or expired OTP");
        }

        user.setEmailVerified(true);
        user.setOtpCode(null);
        user.setOtpExpiry(null);
        userService.saveUserEntity(user);

        logger.info("Email verified for user: {}", user.getId());
    }

    public void sendOtp(String email) {
        var user = userService.getUserEntityByEmail(email);
        if(!user.isEmailVerified()){
            throw new IllegalArgumentException("Unable to send OTP. Please verify your account first");
        }
        if(user.isAccountLocked()){
            throw new IllegalArgumentException("Unable to send OTP at this time. Please try again later");
        }
        otpService.sendOtp(user);
    }

    public void resetPassword(ResetPasswordRequestDTO request) {
        var user = userService.getUserEntityByEmail(request.getEmail());

        if (!otpService.verifyOTP(user, request.getOtpCode())) {
            throw new IllegalArgumentException("Invalid or expired OTP");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setOtpCode(null);
        user.setOtpExpiry(null);
        userService.saveUserEntity(user);

        logger.info("Password reset for user: {}", user.getId());
    }
}
