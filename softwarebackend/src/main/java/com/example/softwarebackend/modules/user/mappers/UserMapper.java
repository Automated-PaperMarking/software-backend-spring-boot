package com.example.softwarebackend.modules.user.mappers;

import com.example.softwarebackend.modules.user.dto.response.UserResponseDTO;
import com.example.softwarebackend.modules.user.entities.User;

public class UserMapper {

    public static UserResponseDTO toUserResponseDTO(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole())
                .emailVerified(user.isEmailVerified())
                .accountLocked(user.isAccountLocked())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
