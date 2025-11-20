package com.example.softwarebackend.modules.user.controllers;

import com.example.softwarebackend.modules.user.dto.request.ChangePasswordRequestDTO;
import com.example.softwarebackend.modules.user.dto.request.UpdateUserRequestDTO;
import com.example.softwarebackend.modules.user.dto.response.UserResponseDTO;
import com.example.softwarebackend.modules.user.services.UserService;
import com.example.softwarebackend.shared.dto.response.ApiResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<ApiResponseDTO<UserResponseDTO>> getUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        UserResponseDTO userProfile = userService.getUserProfile(email);
        ApiResponseDTO<UserResponseDTO> response = ApiResponseDTO.<UserResponseDTO>builder()
                .code("200")
                .message("User profile retrieved successfully")
                .data(userProfile)
                .success(true)
                .build();
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponseDTO<UserResponseDTO>> updateUser(@Valid @RequestBody UpdateUserRequestDTO updateUserRequestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        UserResponseDTO updatedUser = userService.updateUser(email, updateUserRequestDTO);
        ApiResponseDTO<UserResponseDTO> response = ApiResponseDTO.<UserResponseDTO>builder()
                .code("200")
                .message("User profile updated successfully")
                .data(updatedUser)
                .success(true)
                .build();
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/change-password")
    public ResponseEntity<ApiResponseDTO<Void>> changePassword(@Valid @RequestBody ChangePasswordRequestDTO changePasswordRequestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        userService.changePassword(email, changePasswordRequestDTO);
        ApiResponseDTO<Void> response = ApiResponseDTO.<Void>builder()
                .code("200")
                .message("Password changed successfully")
                .data(null)
                .success(true)
                .build();
        return ResponseEntity.ok().body(response);
    }
}
