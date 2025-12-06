package com.library.users.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para validar un token JWT. Se usa tanto como request como response.")
public class TokenValidationDTO {

    @Schema(description = "Token JWT a validar (en request) o token validado (en response)", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...", required = true)
    private String token;

    @Schema(description = "Indica si el token es válido y no ha expirado", example = "true", required = true)
    private boolean valid;

    @Schema(description = "ID del usuario asociado al token (null si el token es inválido)", example = "1")
    private Long userId;

    @Schema(description = "Mensaje descriptivo sobre el resultado de la validación", example = "Token válido", required = true)
    private String message;
}






