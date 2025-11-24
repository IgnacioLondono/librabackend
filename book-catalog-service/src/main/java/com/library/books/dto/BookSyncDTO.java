package com.library.books.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO para sincronización de libros desde Android
 * Permite identificar libros por ISBN o título+autor para actualizar o crear
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookSyncDTO {

    private Long id; // ID del libro si existe en Android (opcional)
    private String title;
    private String author;
    private String isbn; // Usado como identificador único
    private String category;
    private String publisher;
    private Integer year;
    private String description;
    private String coverUrl;
    private Integer totalCopies;
    private Integer availableCopies;
    private BigDecimal price;
    private Boolean featured;
}


