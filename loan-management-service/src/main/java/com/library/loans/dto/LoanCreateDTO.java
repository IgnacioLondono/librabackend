package com.library.loans.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanCreateDTO {

    @NotNull(message = "El ID del usuario es obligatorio")
    @Positive(message = "El ID del usuario debe ser positivo")
    private Long userId;

    @NotNull(message = "El ID del libro es obligatorio")
    @Positive(message = "El ID del libro debe ser positivo")
    private Long bookId;

    private Integer loanDays; // Opcional, usa el valor por defecto si no se proporciona
}






