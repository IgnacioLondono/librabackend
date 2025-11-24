package com.library.loans.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FineCalculationDTO {

    private Long loanId;
    private Integer daysOverdue;
    private BigDecimal dailyFineRate;
    private BigDecimal totalFine;
    private String message;
}


