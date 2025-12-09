package com.library.books.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO de respuesta para operaciones de carga masiva de libros (seed)")
public class SeedResponseDTO {
    
    @Schema(description = "Total de libros procesados", example = "34", requiredMode = Schema.RequiredMode.REQUIRED, minimum = "0")
    private Integer totalProcessed;
    
    @Schema(description = "Libros insertados exitosamente", example = "30", requiredMode = Schema.RequiredMode.REQUIRED, minimum = "0")
    private Integer inserted;
    
    @Schema(description = "Libros que ya existían (omitted)", example = "4", requiredMode = Schema.RequiredMode.REQUIRED, minimum = "0")
    private Integer alreadyExists;
    
    @Schema(description = "Errores durante la carga", example = "0", requiredMode = Schema.RequiredMode.REQUIRED, minimum = "0")
    private Integer errors;
    
    @Schema(description = "Mensaje descriptivo del resultado", example = "Se insertaron 30 libros nuevos. 4 ya existían. 0 errores.", requiredMode = Schema.RequiredMode.REQUIRED)
    private String message;
}


