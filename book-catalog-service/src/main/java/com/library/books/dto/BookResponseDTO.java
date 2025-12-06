package com.library.books.dto;

import com.library.books.model.Book;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO de respuesta con la información completa de un libro")
public class BookResponseDTO {

    @Schema(description = "ID único del libro generado automáticamente", example = "1", required = true)
    private Long id;

    @Schema(description = "Título del libro", example = "1984", required = true)
    private String title;

    @Schema(description = "Nombre completo del autor", example = "George Orwell", required = true)
    private String author;

    @Schema(description = "Número ISBN del libro (único)", example = "9788497593793")
    private String isbn;

    @Schema(description = "Categoría o género del libro", example = "Ciencia Ficción")
    private String category;

    @Schema(description = "Nombre de la editorial", example = "Debolsillo")
    private String publisher;

    @Schema(description = "Año de publicación del libro", example = "1949")
    private Integer year;

    @Schema(description = "Descripción o sinopsis del libro", example = "Una distopía clásica que explora el totalitarismo y la vigilancia estatal.")
    private String description;

    @Schema(description = "URL de la imagen de portada del libro", example = "https://example.com/cover.jpg")
    private String coverUrl;

    @Schema(description = "Estado actual del libro", example = "AVAILABLE", allowableValues = {"AVAILABLE", "LOANED", "RESERVED"}, required = true)
    private Book.Status status;

    @Schema(description = "Número total de copias del libro en la biblioteca", example = "5", required = true)
    private Integer totalCopies;

    @Schema(description = "Número de copias disponibles para préstamo", example = "3", required = true)
    private Integer availableCopies;

    @Schema(description = "Precio del libro en formato decimal", example = "29.99")
    private BigDecimal price;

    @Schema(description = "Indica si el libro está destacado en el catálogo", example = "true", defaultValue = "false")
    private Boolean featured;

    @Schema(description = "Fecha y hora de creación del registro en el sistema", example = "2024-01-15T10:30:00", required = true)
    private LocalDateTime createdAt;

    @Schema(description = "Fecha y hora de la última actualización del registro", example = "2024-01-20T14:45:00")
    private LocalDateTime updatedAt;

    public static BookResponseDTO fromEntity(Book book) {
        return BookResponseDTO.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .isbn(book.getIsbn())
                .category(book.getCategory())
                .publisher(book.getPublisher())
                .year(book.getYear())
                .description(book.getDescription())
                .coverUrl(book.getCoverUrl())
                .status(book.getStatus())
                .totalCopies(book.getTotalCopies())
                .availableCopies(book.getAvailableCopies())
                .price(book.getPrice())
                .featured(book.getFeatured())
                .createdAt(book.getCreatedAt())
                .updatedAt(book.getUpdatedAt())
                .build();
    }
}






