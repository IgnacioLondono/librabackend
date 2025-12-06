package com.library.books.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para actualizar información de un libro existente. Todos los campos son opcionales.")
public class BookUpdateDTO {

    @Size(max = 200, message = "El título no puede exceder 200 caracteres")
    @Schema(description = "Nuevo título del libro", example = "1984", maxLength = 200)
    private String title;

    @Size(max = 100, message = "El autor no puede exceder 100 caracteres")
    @Schema(description = "Nuevo nombre del autor", example = "George Orwell", maxLength = 100)
    private String author;

    @Size(max = 20, message = "El ISBN no puede exceder 20 caracteres")
    @Schema(description = "Nuevo número ISBN (debe ser único)", example = "9788497593793", maxLength = 20)
    private String isbn;

    @Size(max = 50, message = "La categoría no puede exceder 50 caracteres")
    @Schema(description = "Nueva categoría o género", example = "Ciencia Ficción", maxLength = 50)
    private String category;

    @Size(max = 100, message = "La editorial no puede exceder 100 caracteres")
    @Schema(description = "Nueva editorial", example = "Debolsillo", maxLength = 100)
    private String publisher;

    @Schema(description = "Nuevo año de publicación", example = "1949", minimum = "1000", maximum = "2100")
    private Integer year;

    @Size(max = 2000, message = "La descripción no puede exceder 2000 caracteres")
    @Schema(description = "Nueva descripción o sinopsis", example = "Una distopía clásica...", maxLength = 2000)
    private String description;

    @Size(max = 500, message = "La URL de la portada no puede exceder 500 caracteres")
    @Schema(description = "Nueva URL de la imagen de portada", example = "https://example.com/cover.jpg", maxLength = 500)
    private String coverUrl;

    @Positive(message = "El número de copias debe ser positivo")
    @Schema(description = "Nuevo número total de copias (ajusta automáticamente las disponibles)", example = "10", minimum = "1")
    private Integer totalCopies;

    @Schema(description = "Nuevo precio del libro", example = "29.99", minimum = "0")
    private BigDecimal price;

    @Schema(description = "Indica si el libro debe estar destacado", example = "true")
    private Boolean featured;
}






