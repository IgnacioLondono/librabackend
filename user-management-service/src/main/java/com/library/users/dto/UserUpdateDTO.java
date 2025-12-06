package com.library.users.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para actualizar información de un usuario. Todos los campos son opcionales.")
public class UserUpdateDTO {

    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Schema(description = "Nuevo nombre completo del usuario", example = "Juan Carlos Pérez", minLength = 2, maxLength = 100)
    private String name;

    @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
    @Schema(description = "Nuevo número de teléfono de contacto", example = "+56912345678", maxLength = 20)
    private String phone;

    @Size(max = 500, message = "La URI de la imagen no puede exceder 500 caracteres")
    @Schema(description = "Nueva URI de la imagen de perfil del usuario", example = "https://example.com/profile.jpg", maxLength = 500)
    private String profileImageUri;
}






