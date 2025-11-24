package com.library.reports.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardStatisticsDTO {

    private Long totalBooks;
    private Long totalUsers;
    private Long totalLoans;
    private Long activeLoans;
    private Long overdueLoans;
    private Long availableBooks;
    private Long loanedBooks;
    private BigDecimal revenue;
    private String dateRange;
}






