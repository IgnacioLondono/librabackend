package com.library.loans.dto;

import com.library.loans.model.Loan;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO de respuesta con la información completa de un préstamo")
public class LoanResponseDTO {

    @Schema(description = "ID único del préstamo generado automáticamente", example = "1", required = true)
    private Long id;

    @Schema(description = "ID del usuario que tiene el préstamo", example = "5", required = true)
    private Long userId;

    @Schema(description = "ID del libro prestado", example = "10", required = true)
    private Long bookId;

    @Schema(description = "Fecha en que se realizó el préstamo", example = "2024-01-15", required = true)
    private LocalDate loanDate;

    @Schema(description = "Fecha límite para devolver el libro", example = "2024-01-29", required = true)
    private LocalDate dueDate;

    @Schema(description = "Fecha en que se devolvió el libro (null si aún no se ha devuelto)", example = "2024-01-28")
    private LocalDate returnDate;

    @Schema(description = "Estado actual del préstamo", example = "ACTIVE", allowableValues = {"ACTIVE", "RETURNED", "OVERDUE", "CANCELLED"}, required = true)
    private Loan.Status status;

    @Schema(description = "Número de días del préstamo", example = "14", required = true, minimum = "7", maximum = "30")
    private Integer loanDays;

    @Schema(description = "Monto de la multa acumulada (0 si no hay multa)", example = "0.00", required = true, minimum = "0")
    private BigDecimal fineAmount;

    @Schema(description = "Número de extensiones realizadas (máximo 2)", example = "0", required = true, minimum = "0", maximum = "2")
    private Integer extensionsCount;

    @Schema(description = "Fecha y hora de creación del préstamo", example = "2024-01-15T10:30:00", required = true)
    private LocalDateTime createdAt;

    @Schema(description = "Fecha y hora de la última actualización", example = "2024-01-20T14:45:00")
    private LocalDateTime updatedAt;

    public static LoanResponseDTO fromEntity(Loan loan) {
        return LoanResponseDTO.builder()
                .id(loan.getId())
                .userId(loan.getUserId())
                .bookId(loan.getBookId())
                .loanDate(loan.getLoanDate())
                .dueDate(loan.getDueDate())
                .returnDate(loan.getReturnDate())
                .status(loan.getStatus())
                .loanDays(loan.getLoanDays())
                .fineAmount(loan.getFineAmount())
                .extensionsCount(loan.getExtensionsCount())
                .createdAt(loan.getCreatedAt())
                .updatedAt(loan.getUpdatedAt())
                .build();
    }
}






