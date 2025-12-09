package com.library.books.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "DTO para sincronización de libros desde Android. Permite identificar libros por ISBN o título+autor para actualizar o crear.")
public class BookSyncDTO {

    @Schema(description = "ID del libro si existe en Android (opcional, usado para referencia)", example = "1")
    private Long id;

    @Schema(description = "Título del libro", example = "1984", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    @Schema(description = "Autor del libro", example = "George Orwell", requiredMode = Schema.RequiredMode.REQUIRED)
    private String author;

    @Schema(description = "ISBN del libro (usado como identificador único para sincronización)", example = "9788497593793")
    private String isbn;

    @Schema(description = "Categoría o género", example = "Ciencia Ficción")
    private String category;

    @Schema(description = "Editorial", example = "Debolsillo")
    private String publisher;

    @Schema(description = "Año de publicación", example = "1949")
    private Integer year;

    @Schema(description = "Descripción o sinopsis", example = "Una distopía clásica...")
    private String description;

    @Schema(description = "URL de la portada", example = "https://example.com/cover.jpg")
    private String coverUrl;

    @Schema(description = "Total de copias", example = "5", minimum = "1")
    private Integer totalCopies;

    @Schema(description = "Copias disponibles", example = "3", minimum = "0")
    private Integer availableCopies;

    @Schema(description = "Precio del libro", example = "29.99", minimum = "0")
    private BigDecimal price;

    @Schema(description = "Indica si está destacado", example = "false", defaultValue = "false")
    private Boolean featured;
}


