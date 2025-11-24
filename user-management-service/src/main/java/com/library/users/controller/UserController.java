package com.library.users.controller;

import com.library.users.dto.*;
import com.library.users.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "API para gestión de usuarios y autenticación")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    @Operation(summary = "Registrar nuevo usuario", description = "Crea una nueva cuenta de usuario")
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody UserRegistrationDTO registrationDTO) {
        UserResponseDTO user = userService.register(registrationDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión", description = "Autentica un usuario y genera un token JWT")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody UserLoginDTO loginDTO) {
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
    @Operation(summary = "Obtener usuario", description = "Obtiene la información de un usuario por ID")
    public ResponseEntity<UserResponseDTO> getUser(
            @Parameter(description = "ID del usuario") @PathVariable Long userId) {
        UserResponseDTO user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{userId}")
    @Operation(summary = "Actualizar usuario", description = "Actualiza la información de un usuario")
    public ResponseEntity<UserResponseDTO> updateUser(
            @Parameter(description = "ID del usuario") @PathVariable Long userId,
            @Valid @RequestBody UserUpdateDTO updateDTO) {
        UserResponseDTO user = userService.updateUser(userId, updateDTO);
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/{userId}/block")
    @Operation(summary = "Bloquear/desbloquear usuario", description = "Cambia el estado de bloqueo de un usuario")
    public ResponseEntity<UserResponseDTO> blockUser(
            @Parameter(description = "ID del usuario") @PathVariable Long userId,
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
    @Operation(summary = "Validar token", description = "Valida un token JWT")
    public ResponseEntity<TokenValidationDTO> validateToken(@RequestBody TokenValidationDTO tokenDTO) {
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
    @Operation(summary = "Listar usuarios", description = "Obtiene la lista de todos los usuarios")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}





