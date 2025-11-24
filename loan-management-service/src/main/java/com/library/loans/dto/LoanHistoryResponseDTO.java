package com.library.loans.dto;

import com.library.loans.model.LoanHistory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanHistoryResponseDTO {

    private Long id;
    private Long loanId;
    private LoanHistory.Action action;
    private String notes;
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






