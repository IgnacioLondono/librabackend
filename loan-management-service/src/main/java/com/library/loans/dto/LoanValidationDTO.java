package com.library.loans.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanValidationDTO {

    private Long userId;
    private Long bookId;
    private Boolean valid;
    private String message;
    private Boolean userExists;
    private Boolean bookAvailable;
    private Boolean withinLoanLimit; // Usuario tiene menos de 5 préstamos activos
    private Boolean noActiveLoanForBook; // Usuario no tiene préstamo activo del mismo libro
    private Boolean validLoanDays; // Días de préstamo entre 7 y 30
}





