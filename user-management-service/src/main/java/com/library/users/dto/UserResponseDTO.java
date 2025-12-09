package com.library.users.dto;

import com.library.users.model.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Base64;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO de respuesta con la información completa de un usuario")
public class UserResponseDTO {

    @Schema(description = "ID único del usuario generado automáticamente", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Schema(description = "Nombre completo del usuario", example = "Juan Pérez", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Schema(description = "Dirección de correo electrónico (única)", example = "juan.perez@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @Schema(description = "Número de teléfono de contacto", example = "+56912345678")
    private String phone;

    @Schema(description = "Rol del usuario en el sistema", example = "USUARIO", allowableValues = {"USUARIO", "ADMINISTRADOR"}, requiredMode = Schema.RequiredMode.REQUIRED)
    private User.Role role;

    @Schema(description = "Estado del usuario", example = "ACTIVO", allowableValues = {"ACTIVO", "BLOQUEADO"}, requiredMode = Schema.RequiredMode.REQUIRED)
    private User.Status status;

    @Schema(description = "URI de la imagen de perfil del usuario", example = "https://example.com/profile.jpg")
    private String profileImageUri;

    @Schema(description = "Imagen de perfil del usuario en formato Base64 (si está disponible)", example = "data:image/png;base64,iVBORw0KGgoAAAANS...")
    private String profileImageBase64;

    @Schema(description = "Fecha y hora de creación de la cuenta", example = "2024-01-15T10:30:00", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createdAt;

    @Schema(description = "Fecha y hora de la última actualización del perfil", example = "2024-01-20T14:45:00")
    private LocalDateTime updatedAt;

    public static UserResponseDTO fromEntity(User user) {
        String profileImageBase64 = null;
        if (user.getProfileImage() != null && user.getProfileImage().length > 0) {
            try {
                profileImageBase64 = Base64.getEncoder().encodeToString(user.getProfileImage());
            } catch (Exception e) {
                // Si hay error al codificar, se deja como null
            }
        }

        return UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole())
                .status(user.getStatus())
                .profileImageUri(user.getProfileImageUri())
                .profileImageBase64(profileImageBase64)
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}






