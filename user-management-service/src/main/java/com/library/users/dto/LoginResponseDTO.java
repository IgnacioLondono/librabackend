package com.library.users.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO de respuesta del login con token JWT e información del usuario")
public class LoginResponseDTO {

    @Schema(description = "Token JWT para autenticación en peticiones posteriores", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...", required = true)
    private String token;

    @Schema(description = "Información completa del usuario autenticado", required = true)
    private UserResponseDTO user;

    @Schema(description = "Tiempo de expiración del token en milisegundos", example = "86400000", required = true)
    private Long expiresIn; // milisegundos
}






