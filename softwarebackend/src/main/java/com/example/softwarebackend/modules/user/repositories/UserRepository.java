package com.example.softwarebackend.modules.user.repositories;

import com.example.softwarebackend.modules.user.entities.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    //update last login time
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.lastLoginAt = CURRENT_TIMESTAMP WHERE u.id = :id")
    void updateLastLoginTime(@Param("id") UUID id);

    //update user otp and exptime
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.otpCode = :otp, u.otpExpiry = :expTime WHERE u.id = :id")
    void updateUserOtpAndExpTime(@Param("id") UUID id, @Param("otp") String otp, @Param("expTime") LocalDateTime expTime);

    //update account lock details
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.accountLocked = :accountLocked, u.lockTime = :lockTime, u.failedLoginAttempts = :failedLoginAttempts WHERE u.id = :id")
    void updateAccountLockDetails(@Param("id") UUID id, @Param("accountLocked") boolean accountLocked, @Param("lockTime") LocalDateTime lockTime, @Param("failedLoginAttempts") int failedLoginAttempts);
}
