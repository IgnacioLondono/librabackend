package com.library.users.dto;

import com.library.users.model.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO de respuesta con la información completa de un usuario")
public class UserResponseDTO {

    @Schema(description = "ID único del usuario generado automáticamente", example = "1", required = true)
    private Long id;

    @Schema(description = "Nombre completo del usuario", example = "Juan Pérez", required = true)
    private String name;

    @Schema(description = "Dirección de correo electrónico (única)", example = "juan.perez@example.com", required = true)
    private String email;

    @Schema(description = "Número de teléfono de contacto", example = "+56912345678")
    private String phone;

    @Schema(description = "Rol del usuario en el sistema", example = "USUARIO", allowableValues = {"USUARIO", "ADMINISTRADOR"}, required = true)
    private User.Role role;

    @Schema(description = "Estado del usuario", example = "ACTIVO", allowableValues = {"ACTIVO", "BLOQUEADO"}, required = true)
    private User.Status status;

    @Schema(description = "URI de la imagen de perfil del usuario", example = "https://example.com/profile.jpg")
    private String profileImageUri;

    @Schema(description = "Fecha y hora de creación de la cuenta", example = "2024-01-15T10:30:00", required = true)
    private LocalDateTime createdAt;

    @Schema(description = "Fecha y hora de la última actualización del perfil", example = "2024-01-20T14:45:00")
    private LocalDateTime updatedAt;

    public static UserResponseDTO fromEntity(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole())
                .status(user.getStatus())
                .profileImageUri(user.getProfileImageUri())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}






