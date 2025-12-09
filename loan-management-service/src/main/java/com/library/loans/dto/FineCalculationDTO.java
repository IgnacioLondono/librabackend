package com.library.loans.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO con el cálculo de multa para un préstamo vencido")
public class FineCalculationDTO {

    @Schema(description = "ID del préstamo", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long loanId;

    @Schema(description = "Número de días que el préstamo está vencido", example = "5", requiredMode = Schema.RequiredMode.REQUIRED, minimum = "0")
    private Integer daysOverdue;

    @Schema(description = "Tarifa diaria de multa (configurada en el sistema)", example = "1.50", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal dailyFineRate;

    @Schema(description = "Monto total de la multa calculada (días vencidos × tarifa diaria)", example = "7.50", requiredMode = Schema.RequiredMode.REQUIRED, minimum = "0")
    private BigDecimal totalFine;

    @Schema(description = "Mensaje descriptivo sobre el cálculo de la multa", example = "Préstamo vencido hace 5 días", requiredMode = Schema.RequiredMode.REQUIRED)
    private String message;
}


