package com.library.users.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * DTO para estadísticas de auditoría
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO con estadísticas de auditoría del sistema")
public class AuditoriaEstadisticasDTO {
    
    @Schema(description = "Mapa con el conteo de acciones por tipo", example = "{\"CREATE\": 10, \"UPDATE\": 25, \"DELETE\": 5}",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private Map<String, Long> accionesPorTipo;
    
    @Schema(description = "Total de registros de auditoría en el período", example = "40", requiredMode = Schema.RequiredMode.REQUIRED, minimum = "0")
    private Long totalRegistros;
    
    @Schema(description = "Período de tiempo de las estadísticas", example = "Últimos 30 días", requiredMode = Schema.RequiredMode.REQUIRED)
    private String periodo;
}


