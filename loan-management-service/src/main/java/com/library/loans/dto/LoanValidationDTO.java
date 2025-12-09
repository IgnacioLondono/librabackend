package com.library.loans.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO con el resultado de la validación de creación de préstamo. Indica qué validaciones pasaron y cuáles fallaron.")
public class LoanValidationDTO {

    @Schema(description = "ID del usuario que intenta crear el préstamo", example = "1")
    private Long userId;

    @Schema(description = "ID del libro que se intenta prestar", example = "5")
    private Long bookId;

    @Schema(description = "Indica si todas las validaciones pasaron y se puede crear el préstamo", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean valid;

    @Schema(description = "Mensaje descriptivo sobre el resultado de la validación", example = "Validación exitosa", requiredMode = Schema.RequiredMode.REQUIRED)
    private String message;

    @Schema(description = "Indica si el usuario existe y está activo en la base de datos", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean userExists;

    @Schema(description = "Indica si el libro tiene copias disponibles para préstamo", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean bookAvailable;

    @Schema(description = "Indica si el usuario tiene menos de 5 préstamos activos (dentro del límite)", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean withinLoanLimit; // Usuario tiene menos de 5 préstamos activos

    @Schema(description = "Indica si el usuario NO tiene un préstamo activo del mismo libro", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean noActiveLoanForBook; // Usuario no tiene préstamo activo del mismo libro

    @Schema(description = "Indica si los días de préstamo están entre 7 y 30 días", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean validLoanDays; // Días de préstamo entre 7 y 30
}





