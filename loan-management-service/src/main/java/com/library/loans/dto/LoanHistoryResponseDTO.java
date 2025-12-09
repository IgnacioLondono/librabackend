package com.library.loans.dto;

import com.library.loans.model.LoanHistory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO de respuesta con información de un evento en el historial de un préstamo")
public class LoanHistoryResponseDTO {

    @Schema(description = "ID único del registro de historial", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Schema(description = "ID del préstamo al que pertenece este evento", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long loanId;

    @Schema(description = "Acción realizada en el préstamo", example = "CREATED",
            allowableValues = {"CREATED", "RETURNED", "EXTENDED", "CANCELLED", "FINE_APPLIED"},
            requiredMode = Schema.RequiredMode.REQUIRED)
    private LoanHistory.Action action;

    @Schema(description = "Notas adicionales sobre el evento", example = "Préstamo creado exitosamente")
    private String notes;

    @Schema(description = "Fecha y hora en que ocurrió el evento", example = "2024-01-15T10:30:00", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime timestamp;

    public static LoanHistoryResponseDTO fromEntity(LoanHistory history) {
        return LoanHistoryResponseDTO.builder()
                .id(history.getId())
                .loanId(history.getLoanId())
                .action(history.getAction())
                .notes(history.getNotes())
                .timestamp(history.getTimestamp())
                .build();
    }
}






