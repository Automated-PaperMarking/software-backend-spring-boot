package com.example.softwarebackend.modules.otp.services;

import com.example.softwarebackend.shared.entities.User;
import com.example.softwarebackend.modules.user.services.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {
    private static final Logger logger = LoggerFactory.getLogger(OtpServiceImpl.class);

    private final UserService userService;
    private final SecureRandom secureRandom = new SecureRandom();

    @Value("${app.otp.expiry.minutes}")
    private int otpExpiryMinutes;

    public void sendOtp(User user) {
        LocalDateTime currentTime = LocalDateTime.now();
        if (user.getOtpCode() != null && user.getOtpExpiry() != null && user.getOtpExpiry().isAfter(currentTime)) {
            logger.info("OTP already generated and not expired for user: {}", user.getId());
            return;
        }

        String otpCode = String.valueOf(100000 + secureRandom.nextInt(900000));
        LocalDateTime expiryTime = currentTime.plusMinutes(otpExpiryMinutes);
        userService.updateUserOtpAndExpTime(user.getId(), otpCode, expiryTime);

        logger.info("OTP sent successfully for user: {}", user.getId());
    }

    public boolean verifyOTP(User user, String otpCode) {
        LocalDateTime currentTime = LocalDateTime.now();
        if (user.getOtpExpiry().isBefore(currentTime)) {
            return false;
        } else {
            return user.getOtpCode().equals(otpCode);
        }
    }
}
