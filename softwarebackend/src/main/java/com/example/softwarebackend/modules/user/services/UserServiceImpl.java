package com.example.softwarebackend.modules.user.services;

import com.example.softwarebackend.modules.user.dto.request.ChangePasswordRequestDTO;
import com.example.softwarebackend.modules.user.dto.request.UpdateUserRequestDTO;
import com.example.softwarebackend.modules.user.dto.response.UserResponseDTO;
import com.example.softwarebackend.shared.entities.User;
import com.example.softwarebackend.modules.user.mappers.UserMapper;
import com.example.softwarebackend.modules.user.repositories.UserRepository;
import com.example.softwarebackend.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements  UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDTO getUserProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return UserMapper.toUserResponseDTO(user);
    }

    @Override
    public UserResponseDTO updateUser(String email, UpdateUserRequestDTO updateUserRequestDTO) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setFirstName(updateUserRequestDTO.getFirstName());
        user.setLastName(updateUserRequestDTO.getLastName());

        User updatedUser = userRepository.save(user);
        logger.info("User profile updated for: {}", user.getId());

        return UserMapper.toUserResponseDTO(updatedUser);
    }

    @Override
    public void changePassword(String email, ChangePasswordRequestDTO changePasswordRequestDTO) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(changePasswordRequestDTO.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(changePasswordRequestDTO.getNewPassword()));
        userRepository.save(user);

        logger.info("Password changed for user: {}", user.getId());
    }

    @Override
    public Optional<User> getUserEntityByEmail(String email) {
        return userRepository.findByEmail(email);

    }

    @Override
    public User saveUserEntity(User user) {
        return userRepository.save(user);
    }

    @Override
    public void updateAccountLockDetails(UUID id, boolean accountLocked, LocalDateTime lockTime, int failedLoginAttempts) {
        userRepository.updateAccountLockDetails(id, accountLocked, lockTime, failedLoginAttempts);
    }

    @Override
    public void updateLastLoginTime(UUID id) {
        userRepository.updateLastLoginTime(id);
    }

    @Override
    public void updateUserOtpAndExpTime(UUID id, String otpCode, LocalDateTime expiryTime) {
        userRepository.updateUserOtpAndExpTime(id, otpCode, expiryTime);
    }
}
