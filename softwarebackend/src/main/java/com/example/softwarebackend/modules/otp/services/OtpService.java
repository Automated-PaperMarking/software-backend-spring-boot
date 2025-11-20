package com.example.softwarebackend.modules.otp.services;

import com.example.softwarebackend.shared.entities.User;

public interface OtpService {
    public boolean verifyOTP(User user, String otpCode);
    public void sendOtp(User user);
}
