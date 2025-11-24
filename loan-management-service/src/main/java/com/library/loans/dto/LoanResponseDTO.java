package com.library.loans.dto;

import com.library.loans.model.Loan;
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
public class LoanResponseDTO {

    private Long id;
    private Long userId;
    private Long bookId;
    private LocalDate loanDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private Loan.Status status;
    private Integer loanDays;
    private BigDecimal fineAmount;
    private Integer extensionsCount;
    private LocalDateTime createdAt;
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






