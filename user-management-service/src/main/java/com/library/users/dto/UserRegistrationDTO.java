package com.library.users.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para registrar un nuevo usuario en el sistema")
public class UserRegistrationDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Schema(description = "Nombre completo del usuario", example = "Juan Pérez", requiredMode = Schema.RequiredMode.REQUIRED, minLength = 2, maxLength = 100)
    private String name;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe ser válido")
    @Size(max = 100, message = "El email no puede exceder 100 caracteres")
    @Schema(description = "Dirección de correo electrónico (debe ser única)", example = "juan.perez@example.com", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 100)
    private String email;

    @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
    @Schema(description = "Número de teléfono de contacto (opcional)", example = "+56912345678", maxLength = 20)
    private String phone;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, max = 100, message = "La contraseña debe tener entre 6 y 100 caracteres")
    @Schema(description = "Contraseña del usuario (se almacena encriptada con BCrypt)", example = "password123", requiredMode = Schema.RequiredMode.REQUIRED, minLength = 6, maxLength = 100)
    private String password;

    @JsonProperty("profileImageBase64")
    @JsonAlias({"profile_image_base64", "profileImage", "profile_image", "imageBase64", "image_base64"})
    @Schema(description = "Imagen de perfil del usuario en formato Base64 (opcional). Se acepta data URI (data:image/png;base64,...) o solo el string base64. También acepta nombres alternativos: 'profile_image_base64', 'profileImage', 'profile_image', 'imageBase64', 'image_base64'.", example = "data:image/png;base64,iVBORw0KGgoAAAANS...")
    private String profileImageBase64;
}






