package com.library.users.service;

import com.library.users.config.JwtConfig;
import com.library.users.dto.*;
import com.library.users.model.Session;
import com.library.users.model.User;
import com.library.users.repository.SessionRepository;
import com.library.users.repository.UserRepository;
import com.library.users.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final JwtConfig jwtConfig;

    @Transactional
    public UserResponseDTO register(UserRegistrationDTO registrationDTO) {
        log.info("Registrando nuevo usuario: {}", registrationDTO.getEmail());

        if (userRepository.existsByEmail(registrationDTO.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        User user = User.builder()
                .name(registrationDTO.getName())
                .email(registrationDTO.getEmail())
                .phone(registrationDTO.getPhone())
                .password(passwordEncoder.encode(registrationDTO.getPassword()))
                .role(User.Role.USUARIO)
                .status(User.Status.ACTIVO)
                .build();

        user = userRepository.save(user);
        log.info("Usuario registrado exitosamente con ID: {}", user.getId());

        return UserResponseDTO.fromEntity(user);
    }

    @Transactional
    public LoginResponseDTO login(UserLoginDTO loginDTO) {
        log.info("Intento de login para: {}", loginDTO.getEmail());

        User user = userRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));

        if (user.getStatus() == User.Status.BLOQUEADO) {
            throw new RuntimeException("Usuario bloqueado");
        }

        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole().name());
        LocalDateTime expiresAt = LocalDateTime.now().plusSeconds(86400); // 24 horas

        Session session = Session.builder()
                .userId(user.getId())
                .token(token)
                .expiresAt(expiresAt)
                .build();

        sessionRepository.save(session);
        log.info("Sesión creada para usuario: {}", user.getEmail());

        return LoginResponseDTO.builder()
                .token(token)
                .user(UserResponseDTO.fromEntity(user))
                .expiresIn(jwtConfig.getExpiration())
                .build();
    }

    @Transactional
    public void logout(String token) {
        log.info("Cerrando sesión");
        sessionRepository.findByToken(token)
                .ifPresent(sessionRepository::delete);
    }

    public UserResponseDTO getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return UserResponseDTO.fromEntity(user);
    }

    @Transactional
    public UserResponseDTO updateUser(Long userId, UserUpdateDTO updateDTO) {
        log.info("Actualizando usuario: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (updateDTO.getName() != null) {
            user.setName(updateDTO.getName());
        }
        if (updateDTO.getPhone() != null) {
            user.setPhone(updateDTO.getPhone());
        }
        if (updateDTO.getProfileImageUri() != null) {
            user.setProfileImageUri(updateDTO.getProfileImageUri());
        }

        user = userRepository.save(user);
        return UserResponseDTO.fromEntity(user);
    }

    @Transactional
    public UserResponseDTO blockUser(Long userId, Boolean blocked) {
        log.info("Cambiando estado de bloqueo para usuario {} a {}", userId, blocked);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        user.setStatus(blocked ? User.Status.BLOQUEADO : User.Status.ACTIVO);

        user = userRepository.save(user);

        // Invalidar todas las sesiones del usuario si se bloquea
        if (user.getStatus() == User.Status.BLOQUEADO) {
            sessionRepository.deleteByUserId(userId);
            log.info("Sesiones invalidadas para usuario bloqueado: {}", userId);
        }

        return UserResponseDTO.fromEntity(user);
    }

    @Transactional
    public UserResponseDTO changeRole(Long userId, User.Role newRole) {
        log.info("Cambiando rol de usuario {} a {}", userId, newRole);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        user.setRole(newRole);
        user = userRepository.save(user);

        return UserResponseDTO.fromEntity(user);
    }

    public TokenValidationDTO validateToken(String token) {
        try {
            if (jwtUtil.validateToken(token)) {
                Long userId = jwtUtil.extractUserId(token);
                return new TokenValidationDTO(token, true, userId, "Token válido");
            } else {
                return new TokenValidationDTO(token, false, null, "Token expirado o inválido");
            }
        } catch (Exception e) {
            log.error("Error validando token: {}", e.getMessage());
            return new TokenValidationDTO(token, false, null, "Error al validar token: " + e.getMessage());
        }
    }

    @Transactional
    public void deleteUser(Long userId) {
        log.info("Eliminando usuario: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        sessionRepository.deleteByUserId(userId);
        userRepository.delete(user);
    }

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }
}

