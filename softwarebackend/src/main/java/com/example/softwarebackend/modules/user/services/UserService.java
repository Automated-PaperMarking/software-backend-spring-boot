package com.example.softwarebackend.modules.user.services;


import com.example.softwarebackend.modules.user.dto.request.ChangePasswordRequestDTO;
import com.example.softwarebackend.modules.user.dto.request.UpdateUserRequestDTO;
import com.example.softwarebackend.modules.user.dto.response.UserResponseDTO;
import com.example.softwarebackend.modules.user.entities.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public interface UserService {


    public UserResponseDTO getUserProfile(String email);

    public UserResponseDTO updateUser(String email, UpdateUserRequestDTO updateUserRequestDTO);

    public void changePassword(String email, ChangePasswordRequestDTO changePasswordRequestDTO) ;

    public User getUserEntityByEmail(String email);

    public User saveUserEntity(User user);

    public void updateAccountLockDetails( UUID id, boolean accountLocked,  LocalDateTime lockTime,  int failedLoginAttempts);


    void updateLastLoginTime(UUID id);

    void updateUserOtpAndExpTime(UUID id, String otpCode, LocalDateTime expiryTime);
}
