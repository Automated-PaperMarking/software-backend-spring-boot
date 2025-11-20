package com.example.softwarebackend.modules.auth.mappers;

import com.example.softwarebackend.modules.auth.dto.request.RegisterRequestDTO;
import com.example.softwarebackend.modules.auth.dto.response.UserSummaryDTO;
import com.example.softwarebackend.shared.entities.User;
import com.example.softwarebackend.shared.enums.Role;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.OffsetDateTime;

public class AuthenticationMapper {

    public static User toUserFromRegisterRequestDTO(RegisterRequestDTO registerRequestDTO, PasswordEncoder passwordEncoder) {
        return User.builder()
                .firstName(registerRequestDTO.getFirstName())
                .lastName(registerRequestDTO.getLastName())
                .email(registerRequestDTO.getEmail())
                .password(passwordEncoder.encode(registerRequestDTO.getPassword()))
                .role(Role.STUDENT)
                .emailVerified(false)
                .accountLocked(false)
                .failedLoginAttempts(0)
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();
    }

    public static UserSummaryDTO toUserSummaryDTO(User user) {
        return UserSummaryDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole())
                .emailVerified(user.isEmailVerified())
                .build();
    }
}
