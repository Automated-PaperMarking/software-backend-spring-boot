package com.example.softwarebackend.modules.auth.services;

import com.example.softwarebackend.modules.auth.dto.request.RegisterRequestDTO;
import com.example.softwarebackend.modules.otp.services.OtpService;
import com.example.softwarebackend.modules.user.services.UserService;
import com.example.softwarebackend.shared.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@DisplayName("Authentication Service Unit Test")
class AuthenticationServiceTest {

    @Mock
    private UserService userService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private OtpService otpService;

    @InjectMocks
    private AuthenticationService authenticationService;

    private RegisterRequestDTO registerRequestDTO;
    private User user;


    @BeforeEach
    void setUp() {
        this.registerRequestDTO = new RegisterRequestDTO("John", "Doe", "john@gmail.com", "Password@123");
        this.user = User.builder().firstName("John").lastName("Doe").email("john@gmail.com").password("encodedPassword").emailVerified(false).failedLoginAttempts(0).accountLocked(false).build();
    }

    @Nested
    @DisplayName("Test user registration")
    class RegisterUserTest {
        @Test
        @DisplayName("Registration succeeds with valid details")
        void registrationShouldSuccess() {
            //given
            when(userService.getUserEntityByEmail(registerRequestDTO.getEmail())).thenReturn(Optional.empty());
            when(passwordEncoder.encode(registerRequestDTO.getPassword())).thenReturn("encodedPassword");
            when(userService.saveUserEntity(any(User.class))).thenReturn(user);
            //when
            authenticationService.registerUser(registerRequestDTO);
            //then  , (role of verify is to check whether a method is called or not)
            verify(userService, times(1)).getUserEntityByEmail(registerRequestDTO.getEmail());
            verify(userService, times(1)).saveUserEntity(any(User.class));
            verify(otpService, times(1)).sendOtp(user);
            verify(passwordEncoder, times(1)).encode(registerRequestDTO.getPassword());

        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when email already exists")
        void registrationShouldFailWhenEmailExists() {
            //given
            when(userService.getUserEntityByEmail(registerRequestDTO.getEmail())).thenReturn(Optional.of(user));
            //when - then
            final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                authenticationService.registerUser(registerRequestDTO);
            });
            //then
            assertEquals("Registration failed. Please try again with different details", exception.getMessage());
            verify(userService, times(1)).getUserEntityByEmail(registerRequestDTO.getEmail());
            verify(userService, never()).saveUserEntity(any(User.class));
            verifyNoInteractions(passwordEncoder);
            verifyNoInteractions(otpService);
        }
    }

}