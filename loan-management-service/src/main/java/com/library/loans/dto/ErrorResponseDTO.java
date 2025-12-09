package com.library.loans.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO de respuesta para errores del sistema")
public class ErrorResponseDTO {

    @Schema(description = "Fecha y hora en que ocurrió el error", example = "2024-01-15T10:30:00", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime timestamp;

    @Schema(description = "Código de estado HTTP", example = "400", requiredMode = Schema.RequiredMode.REQUIRED)
    private int status;

    @Schema(description = "Tipo de error", example = "Bad Request", requiredMode = Schema.RequiredMode.REQUIRED)
    private String error;

    @Schema(description = "Mensaje descriptivo del error", example = "El usuario no puede tener más de 5 préstamos activos", requiredMode = Schema.RequiredMode.REQUIRED)
    private String message;

    @Schema(description = "Ruta del endpoint donde ocurrió el error", example = "/api/loans", requiredMode = Schema.RequiredMode.REQUIRED)
    private String path;

    @Schema(description = "Lista de detalles adicionales del error (validaciones, etc.)")
    private List<String> details;
}






