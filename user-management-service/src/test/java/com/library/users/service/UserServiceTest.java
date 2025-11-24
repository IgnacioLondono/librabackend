package com.library.users.service;

import com.library.users.config.JwtConfig;
import com.library.users.dto.UserLoginDTO;
import com.library.users.dto.UserRegistrationDTO;
import com.library.users.dto.UserResponseDTO;
import com.library.users.model.User;
import com.library.users.repository.SessionRepository;
import com.library.users.repository.UserRepository;
import com.library.users.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private JwtConfig jwtConfig;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private UserRegistrationDTO registrationDTO;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .phone("123456789")
                .password("encodedPassword")
                .role(User.Role.USUARIO)
                .status(User.Status.ACTIVO)
                .build();

        registrationDTO = new UserRegistrationDTO();
        registrationDTO.setName("Test User");
        registrationDTO.setEmail("test@example.com");
        registrationDTO.setPhone("123456789");
        registrationDTO.setPassword("password123");
    }

    @Test
    void testRegister_Success() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        UserResponseDTO result = userService.register(registrationDTO);

        assertNotNull(result);
        assertEquals(testUser.getEmail(), result.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegister_EmailExists() {
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(RuntimeException.class, () -> userService.register(registrationDTO));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testLogin_Success() {
        UserLoginDTO loginDTO = new UserLoginDTO();
        loginDTO.setEmail("test@example.com");
        loginDTO.setPassword("password123");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtUtil.generateToken(anyLong(), anyString(), anyString())).thenReturn("testToken");
        when(jwtConfig.getExpiration()).thenReturn(86400L);

        var result = userService.login(loginDTO);

        assertNotNull(result);
        assertEquals("testToken", result.getToken());
        assertNotNull(result.getUser());
        assertEquals(86400L, result.getExpiresIn());
        verify(sessionRepository, times(1)).save(any());
    }

    @Test
    void testLogin_InvalidCredentials() {
        UserLoginDTO loginDTO = new UserLoginDTO();
        loginDTO.setEmail("test@example.com");
        loginDTO.setPassword("wrongPassword");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        assertThrows(RuntimeException.class, () -> userService.login(loginDTO));
    }
}





