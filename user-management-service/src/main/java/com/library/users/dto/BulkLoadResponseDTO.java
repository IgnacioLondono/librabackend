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
@Schema(description = "DTO de respuesta para carga masiva de usuarios")
public class BulkLoadResponseDTO {
    
    @Schema(description = "Total de usuarios procesados", example = "10")
    private int totalProcessed;
    
    @Schema(description = "Usuarios insertados exitosamente", example = "8")
    private int inserted;
    
    @Schema(description = "Usuarios que ya existían (omitted)", example = "2")
    private int alreadyExists;
    
    @Schema(description = "Errores durante la carga", example = "0")
    private int errors;
    
    @Schema(description = "Mensaje descriptivo del resultado", example = "Se insertaron 8 usuarios nuevos. 2 ya existían. 0 errores.")
    private String message;
}







