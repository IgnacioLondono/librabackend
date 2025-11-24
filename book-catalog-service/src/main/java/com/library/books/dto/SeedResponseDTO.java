package com.library.books.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeedResponseDTO {
    private Integer totalProcessed;
    private Integer inserted;
    private Integer alreadyExists;
    private Integer errors;
    private String message;
}


