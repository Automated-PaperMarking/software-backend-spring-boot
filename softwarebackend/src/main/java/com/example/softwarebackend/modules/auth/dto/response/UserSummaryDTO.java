package com.example.softwarebackend.modules.auth.dto.response;

import com.example.softwarebackend.shared.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSummaryDTO {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private Role role;
    private boolean emailVerified;
}
