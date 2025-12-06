package com.library.loans.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para crear un nuevo préstamo de libro")
public class LoanCreateDTO {

    @NotNull(message = "El ID del usuario es obligatorio")
    @Positive(message = "El ID del usuario debe ser positivo")
    @Schema(description = "ID del usuario que solicita el préstamo", example = "1", required = true, minimum = "1")
    private Long userId;

    @NotNull(message = "El ID del libro es obligatorio")
    @Positive(message = "El ID del libro debe ser positivo")
    @Schema(description = "ID del libro a prestar", example = "5", required = true, minimum = "1")
    private Long bookId;

    @Schema(description = "Número de días del préstamo (opcional, por defecto 14 días). Debe estar entre 7 y 30 días", example = "14", minimum = "7", maximum = "30", defaultValue = "14")
    private Integer loanDays; // Opcional, usa el valor por defecto si no se proporciona
}






