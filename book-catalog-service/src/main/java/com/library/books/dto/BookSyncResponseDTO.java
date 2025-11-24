package com.library.books.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Respuesta de sincronización masiva
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookSyncResponseDTO {

    private Integer totalProcessed;
    private Integer created;
    private Integer updated;
    private Integer skipped;
    private Integer errors;
    private List<BookResponseDTO> createdBooks;
    private List<BookResponseDTO> updatedBooks;
    private List<String> errorMessages;
}


