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
import java.util.Base64;
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

        // Procesar imagen de perfil si está presente
        byte[] profileImageBytes = null;
        boolean hasImageData = registrationDTO.getProfileImageBase64() != null && !registrationDTO.getProfileImageBase64().trim().isEmpty();
        log.info("Imagen de perfil recibida: {} (longitud del string: {})", 
                hasImageData ? "SÍ" : "NO", 
                hasImageData ? registrationDTO.getProfileImageBase64().length() : 0);
        
        if (hasImageData) {
            try {
                String base64Image = registrationDTO.getProfileImageBase64().trim();
                log.info("Procesando imagen base64. Prefijo data URI presente: {}", base64Image.contains(","));
                
                // Remover el prefijo data URI si está presente (data:image/png;base64,...)
                if (base64Image.contains(",")) {
                    base64Image = base64Image.substring(base64Image.indexOf(",") + 1);
                    log.info("Prefijo data URI removido. Longitud restante: {}", base64Image.length());
                }
                
                profileImageBytes = Base64.getDecoder().decode(base64Image);
                log.info("Imagen de perfil decodificada exitosamente. Tamaño: {} bytes", profileImageBytes.length);
            } catch (IllegalArgumentException e) {
                log.error("ERROR al decodificar imagen base64 para usuario {}: {}", registrationDTO.getEmail(), e.getMessage(), e);
                // Continuar sin imagen si hay error en la decodificación
            } catch (Exception e) {
                log.error("ERROR inesperado al procesar imagen para usuario {}: {}", registrationDTO.getEmail(), e.getMessage(), e);
            }
        }

        User.UserBuilder userBuilder = User.builder()
                .name(registrationDTO.getName())
                .email(registrationDTO.getEmail())
                .phone(registrationDTO.getPhone())
                .password(passwordEncoder.encode(registrationDTO.getPassword()))
                .role(User.Role.USUARIO)
                .status(User.Status.ACTIVO);

        if (profileImageBytes != null) {
            userBuilder.profileImage(profileImageBytes);
            log.info("Imagen agregada al builder. Tamaño: {} bytes", profileImageBytes.length);
        } else {
            log.info("No se agregó imagen al builder (profileImageBytes es null)");
        }

        User user = userBuilder.build();
        log.info("Usuario construido. Imagen en entidad: {} (tamaño: {} bytes)", 
                user.getProfileImage() != null ? "SÍ" : "NO",
                user.getProfileImage() != null ? user.getProfileImage().length : 0);
        
        user = userRepository.save(user);
        
        // Verificar después de guardar
        User savedUser = userRepository.findById(user.getId()).orElse(null);
        if (savedUser != null) {
            log.info("Usuario guardado. Imagen en BD: {} (tamaño: {} bytes)", 
                    savedUser.getProfileImage() != null ? "SÍ" : "NO",
                    savedUser.getProfileImage() != null ? savedUser.getProfileImage().length : 0);
        }
        
        log.info("Usuario registrado exitosamente con ID: {} (imagen: {})", user.getId(), profileImageBytes != null ? "sí" : "no");

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

        // Procesar imagen de perfil si está presente
        if (updateDTO.getProfileImageBase64() != null && !updateDTO.getProfileImageBase64().trim().isEmpty()) {
            try {
                String base64Image = updateDTO.getProfileImageBase64().trim();
                // Remover el prefijo data URI si está presente
                if (base64Image.contains(",")) {
                    base64Image = base64Image.substring(base64Image.indexOf(",") + 1);
                }
                byte[] profileImageBytes = Base64.getDecoder().decode(base64Image);
                user.setProfileImage(profileImageBytes);
                log.debug("Imagen de perfil actualizada. Tamaño: {} bytes", profileImageBytes.length);
            } catch (IllegalArgumentException e) {
                log.warn("Error al decodificar imagen base64 para usuario {}: {}", userId, e.getMessage());
                // No actualizar la imagen si hay error
            }
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

        // NOTA: Para una validación completa, se debería consultar el servicio de préstamos
        // para verificar si el usuario tiene préstamos activos antes de eliminar.
        // Por ahora, se elimina directamente. Se recomienda implementar un endpoint
        // en LoanService para verificar préstamos activos por userId y usar un cliente HTTP aquí.

        // Eliminar sesiones del usuario
        sessionRepository.deleteByUserId(userId);
        
        // Eliminar usuario
        userRepository.delete(user);
        log.info("Usuario {} eliminado exitosamente", userId);
    }

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public byte[] getProfileImage(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return user.getProfileImage();
    }

    @Transactional
    public BulkLoadResponseDTO loadUsersBulk(List<UserRegistrationDTO> users) {
        log.info("Iniciando carga masiva de {} usuarios", users.size());
        
        int totalProcessed = users.size();
        int inserted = 0;
        int alreadyExists = 0;
        int errors = 0;

        for (UserRegistrationDTO registrationDTO : users) {
            try {
                if (userRepository.existsByEmail(registrationDTO.getEmail())) {
                    alreadyExists++;
                    log.debug("Usuario ya existe: {}", registrationDTO.getEmail());
                    continue;
                }

                // Convertir role string a enum
                User.Role role = User.Role.USUARIO;
                if (registrationDTO.getEmail() != null && 
                    (registrationDTO.getEmail().contains("admin") || 
                     registrationDTO.getEmail().equals("admin123@gmail.com"))) {
                    role = User.Role.ADMINISTRADOR;
                }

                // Procesar imagen de perfil si está presente
                byte[] profileImageBytes = null;
                if (registrationDTO.getProfileImageBase64() != null && !registrationDTO.getProfileImageBase64().trim().isEmpty()) {
                    try {
                        String base64Image = registrationDTO.getProfileImageBase64().trim();
                        // Remover el prefijo data URI si está presente
                        if (base64Image.contains(",")) {
                            base64Image = base64Image.substring(base64Image.indexOf(",") + 1);
                        }
                        profileImageBytes = Base64.getDecoder().decode(base64Image);
                    } catch (IllegalArgumentException e) {
                        log.warn("Error al decodificar imagen base64 para usuario {}: {}", registrationDTO.getEmail(), e.getMessage());
                    }
                }

                User.UserBuilder userBuilder = User.builder()
                        .name(registrationDTO.getName())
                        .email(registrationDTO.getEmail())
                        .phone(registrationDTO.getPhone())
                        .password(passwordEncoder.encode(registrationDTO.getPassword()))
                        .role(role)
                        .status(User.Status.ACTIVO);

                if (profileImageBytes != null) {
                    userBuilder.profileImage(profileImageBytes);
                }

                User user = userBuilder.build();
                userRepository.save(user);
                inserted++;
                log.debug("Usuario insertado: {}", registrationDTO.getEmail());
            } catch (Exception e) {
                errors++;
                log.error("Error insertando usuario {}: {}", registrationDTO.getEmail(), e.getMessage());
            }
        }

        String message = String.format("Se insertaron %d usuarios nuevos. %d ya existían. %d errores.",
                inserted, alreadyExists, errors);

        log.info("Carga masiva de usuarios completada: {}", message);

        return BulkLoadResponseDTO.builder()
                .totalProcessed(totalProcessed)
                .inserted(inserted)
                .alreadyExists(alreadyExists)
                .errors(errors)
                .message(message)
                .build();
    }
}

