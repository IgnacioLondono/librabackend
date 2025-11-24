package com.library.users.dto;

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
public class AuditoriaEstadisticasDTO {
    
    private Map<String, Long> accionesPorTipo;
    private Long totalRegistros;
    private String periodo;
}


