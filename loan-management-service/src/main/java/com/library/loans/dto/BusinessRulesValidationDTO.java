package com.library.loans.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO con el resultado de la validación de todas las reglas de negocio del sistema de préstamos")
public class BusinessRulesValidationDTO {

    @Schema(description = "Indica si todas las reglas de negocio son válidas", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean allRulesValid;

    @Schema(description = "Resumen general de la validación", example = "Todas las reglas de negocio están correctamente implementadas", requiredMode = Schema.RequiredMode.REQUIRED)
    private String summary;

    @Schema(description = "Lista detallada de validación de cada regla", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<RuleValidation> rules;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "Validación individual de una regla de negocio")
    public static class RuleValidation {
        
        @Schema(description = "Nombre de la regla", example = "Límite de préstamos activos", requiredMode = Schema.RequiredMode.REQUIRED)
        private String ruleName;
        
        @Schema(description = "Descripción de la regla", example = "Un usuario no puede tener más de 5 préstamos activos simultáneamente", requiredMode = Schema.RequiredMode.REQUIRED)
        private String description;
        
        @Schema(description = "Indica si la regla es válida", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
        private Boolean valid;
        
        @Schema(description = "Mensaje sobre el estado de la validación", example = "Regla implementada correctamente", requiredMode = Schema.RequiredMode.REQUIRED)
        private String message;
    }
}

