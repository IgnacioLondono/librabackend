package com.library.reports.dto;

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
@Schema(description = "DTO con estadísticas del dashboard del sistema de biblioteca")
public class DashboardStatisticsDTO {

    @Schema(description = "Total de libros en el catálogo", example = "34", requiredMode = Schema.RequiredMode.REQUIRED, minimum = "0")
    private Long totalBooks;

    @Schema(description = "Total de usuarios registrados", example = "150", requiredMode = Schema.RequiredMode.REQUIRED, minimum = "0")
    private Long totalUsers;

    @Schema(description = "Total de préstamos realizados (histórico)", example = "500", requiredMode = Schema.RequiredMode.REQUIRED, minimum = "0")
    private Long totalLoans;

    @Schema(description = "Préstamos activos actualmente", example = "45", requiredMode = Schema.RequiredMode.REQUIRED, minimum = "0")
    private Long activeLoans;

    @Schema(description = "Préstamos vencidos (no devueltos a tiempo)", example = "5", requiredMode = Schema.RequiredMode.REQUIRED, minimum = "0")
    private Long overdueLoans;

    @Schema(description = "Libros disponibles para préstamo", example = "200", requiredMode = Schema.RequiredMode.REQUIRED, minimum = "0")
    private Long availableBooks;

    @Schema(description = "Libros actualmente prestados", example = "30", requiredMode = Schema.RequiredMode.REQUIRED, minimum = "0")
    private Long loanedBooks;

    @Schema(description = "Ingresos por multas acumuladas", example = "1250.50", requiredMode = Schema.RequiredMode.REQUIRED, minimum = "0")
    private BigDecimal revenue;

    @Schema(description = "Rango de fechas de las estadísticas", example = "Últimos 30 días", requiredMode = Schema.RequiredMode.REQUIRED)
    private String dateRange;
}






