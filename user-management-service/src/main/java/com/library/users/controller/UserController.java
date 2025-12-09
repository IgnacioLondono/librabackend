package com.library.users.controller;

import com.library.users.dto.*;
import com.library.users.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "User Management", description = "API para gestión de usuarios y autenticación")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    @Operation(
        summary = "Registrar nuevo usuario", 
        description = "Crea una nueva cuenta de usuario en el sistema. El email debe ser único. " +
                     "La contraseña se almacena encriptada con BCrypt. El usuario se crea con rol USUARIO y estado ACTIVO por defecto. " +
                     "Puede incluir una imagen de perfil en formato Base64 (campo opcional 'profileImageBase64'). " +
                     "La imagen se acepta como data URI (data:image/png;base64,...) o solo el string base64. " +
                     "No requiere autenticación.",
        responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "201", 
                description = "Usuario registrado exitosamente",
                content = @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = UserResponseDTO.class)
                )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "400", 
                description = "Datos inválidos o email ya registrado"
            )
        }
    )
    public ResponseEntity<UserResponseDTO> register(
            @Parameter(description = "Datos del nuevo usuario. Email, nombre y contraseña son obligatorios.", required = true)
            @Valid @RequestBody UserRegistrationDTO registrationDTO) {
        // Logging detallado para diagnosticar el problema
        log.info("=== REGISTRO DE USUARIO - DATOS RECIBIDOS ===");
        log.info("Email: {}", registrationDTO.getEmail());
        log.info("Nombre: {}", registrationDTO.getName());
        log.info("Teléfono: {}", registrationDTO.getPhone());
        log.info("Password presente: {}", registrationDTO.getPassword() != null ? "SÍ" : "NO");
        log.info("profileImageBase64 presente: {}", registrationDTO.getProfileImageBase64() != null ? "SÍ" : "NO");
        if (registrationDTO.getProfileImageBase64() != null) {
            log.info("profileImageBase64 longitud: {} caracteres", registrationDTO.getProfileImageBase64().length());
            log.info("profileImageBase64 primeros 100 caracteres: {}", 
                    registrationDTO.getProfileImageBase64().length() > 100 
                        ? registrationDTO.getProfileImageBase64().substring(0, 100) + "..." 
                        : registrationDTO.getProfileImageBase64());
        }
        log.info("=============================================");
        
        UserResponseDTO user = userService.register(registrationDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PostMapping("/login")
    @Operation(
        summary = "Iniciar sesión", 
        description = "Autentica un usuario con email y contraseña. Si las credenciales son válidas y el usuario no está bloqueado, " +
                     "genera un token JWT que expira en 24 horas. El token debe incluirse en el header 'Authorization: Bearer {token}' " +
                     "para acceder a endpoints protegidos. También crea una sesión en la base de datos. " +
                     "IMPORTANTE: El admin debe estar en la BD local para poder hacer login.",
        responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200", 
                description = "Login exitoso, token JWT generado",
                content = @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = LoginResponseDTO.class)
                )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "400", 
                description = "Credenciales inválidas o usuario bloqueado"
            )
        }
    )
    public ResponseEntity<LoginResponseDTO> login(
            @Parameter(description = "Credenciales de acceso (email y contraseña)", required = true)
            @Valid @RequestBody UserLoginDTO loginDTO) {
        LoginResponseDTO response = userService.login(loginDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    @Operation(summary = "Cerrar sesión", description = "Invalida la sesión del usuario actual")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        String token = extractToken(request);
        userService.logout(token);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}")
    @Operation(
        summary = "Obtener usuario", 
        description = "Obtiene la información completa de un usuario por su ID. Requiere autenticación. " +
                     "Solo se puede acceder si el usuario existe en la base de datos.",
        responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200", 
                description = "Usuario encontrado",
                content = @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = UserResponseDTO.class)
                )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "404", 
                description = "Usuario no encontrado"
            )
        }
    )
    public ResponseEntity<UserResponseDTO> getUser(
            @Parameter(description = "ID único del usuario", example = "1", required = true) @PathVariable Long userId) {
        UserResponseDTO user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{userId}")
    @Operation(
        summary = "Actualizar usuario", 
        description = "Actualiza la información de un usuario existente. Todos los campos son opcionales, " +
                     "solo se actualizan los campos proporcionados. Puede incluir una nueva imagen de perfil " +
                     "en formato Base64 (campo opcional 'profileImageBase64'). Requiere autenticación.",
        responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200", 
                description = "Usuario actualizado exitosamente"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "404", 
                description = "Usuario no encontrado"
            )
        }
    )
    public ResponseEntity<UserResponseDTO> updateUser(
            @Parameter(description = "ID del usuario a actualizar", example = "1", required = true) @PathVariable Long userId,
            @Parameter(description = "Datos a actualizar (todos los campos son opcionales)", required = true)
            @Valid @RequestBody UserUpdateDTO updateDTO) {
        UserResponseDTO user = userService.updateUser(userId, updateDTO);
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/{userId}/block")
    @Operation(
        summary = "Bloquear/desbloquear usuario", 
        description = "Cambia el estado de bloqueo de un usuario. Si se bloquea, se invalidan automáticamente " +
                     "todas las sesiones activas del usuario. Requiere rol de administrador.",
        responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200", 
                description = "Estado de bloqueo actualizado exitosamente"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "403", 
                description = "No tiene permisos de administrador"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "404", 
                description = "Usuario no encontrado"
            )
        }
    )
    public ResponseEntity<UserResponseDTO> blockUser(
            @Parameter(description = "ID del usuario a bloquear/desbloquear", example = "1", required = true) @PathVariable Long userId,
            @Parameter(description = "true para bloquear, false para desbloquear", required = true)
            @Valid @RequestBody BlockUserDTO blockDTO) {
        UserResponseDTO user = userService.blockUser(userId, blockDTO.getBlocked());
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/{userId}/role")
    @Operation(summary = "Cambiar rol", description = "Cambia el rol de un usuario")
    public ResponseEntity<UserResponseDTO> changeRole(
            @Parameter(description = "ID del usuario") @PathVariable Long userId,
            @Valid @RequestBody ChangeRoleDTO roleDTO) {
        UserResponseDTO user = userService.changeRole(userId, roleDTO.getRole());
        return ResponseEntity.ok(user);
    }

    @PostMapping("/validate-token")
    @Operation(
        summary = "Validar token", 
        description = "Valida un token JWT y verifica si está activo y no ha expirado. " +
                     "Retorna información sobre la validez del token, el ID del usuario asociado y un mensaje descriptivo. " +
                     "No requiere autenticación.",
        responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200", 
                description = "Validación completada",
                content = @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = TokenValidationDTO.class)
                )
            )
        }
    )
    public ResponseEntity<TokenValidationDTO> validateToken(
            @Parameter(description = "Token JWT a validar", required = true)
            @RequestBody TokenValidationDTO tokenDTO) {
        TokenValidationDTO result = userService.validateToken(tokenDTO.getToken());
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario del sistema")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID del usuario") @PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(
        summary = "Listar usuarios", 
        description = "Obtiene la lista completa de todos los usuarios registrados en el sistema. " +
                     "Requiere autenticación. Solo retorna usuarios que existen en la base de datos.",
        responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200", 
                description = "Lista de usuarios obtenida exitosamente"
            )
        }
    )
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/bulk")
    @Operation(
        summary = "Cargar usuarios en lote", 
        description = "Carga múltiples usuarios desde un JSON. Las contraseñas se encriptan automáticamente. " +
                     "Si un usuario ya existe (por email), se omite. Útil para cargar datos iniciales desde Android. " +
                     "Puede incluir imágenes de perfil en formato Base64 para cada usuario.",
        responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200", 
                description = "Usuarios cargados exitosamente"
            )
        }
    )
    public ResponseEntity<BulkLoadResponseDTO> loadUsersBulk(
            @Parameter(description = "Lista de usuarios a cargar", required = true)
            @Valid @RequestBody List<UserRegistrationDTO> users) {
        BulkLoadResponseDTO response = userService.loadUsersBulk(users);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}/profile-image")
    @Operation(
        summary = "Obtener imagen de perfil", 
        description = "Obtiene la imagen de perfil de un usuario como archivo binario. " +
                     "Retorna la imagen en su formato original (PNG, JPEG, etc.) o 404 si el usuario no tiene imagen.",
        responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200", 
                description = "Imagen de perfil obtenida exitosamente",
                content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "image/png")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "404", 
                description = "Usuario no encontrado o no tiene imagen de perfil"
            )
        }
    )
    public ResponseEntity<byte[]> getProfileImage(
            @Parameter(description = "ID único del usuario", example = "1", required = true) 
            @PathVariable Long userId) {
        byte[] imageBytes = userService.getProfileImage(userId);
        if (imageBytes == null || imageBytes.length == 0) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG); // Por defecto PNG, se puede detectar el tipo real
        headers.setContentLength(imageBytes.length);
        headers.setCacheControl("public, max-age=3600"); // Cache por 1 hora

        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}





