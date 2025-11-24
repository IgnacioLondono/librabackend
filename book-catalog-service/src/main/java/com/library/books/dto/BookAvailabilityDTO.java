package com.library.books.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookAvailabilityDTO {

    private Long bookId;
    private Boolean available;
    private Integer availableCopies;
    private Integer totalCopies;
    private String message;
}






