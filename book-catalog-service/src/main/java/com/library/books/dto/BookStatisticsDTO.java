package com.library.books.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookStatisticsDTO {

    private Long totalBooks;
    private Long availableBooks;
    private Long loanedBooks;
    private Long reservedBooks;
    private Long totalCopies;
    private Long availableCopies;
}


