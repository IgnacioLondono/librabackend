package com.library.books.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO para crear un nuevo libro en el catálogo")
public class BookCreateDTO {

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 200, message = "El título no puede exceder 200 caracteres")
    @Schema(description = "Título del libro", example = "1984", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 200)
    private String title;

    @NotBlank(message = "El autor es obligatorio")
    @Size(max = 100, message = "El autor no puede exceder 100 caracteres")
    @Schema(description = "Nombre completo del autor", example = "George Orwell", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 100)
    private String author;

    @Size(max = 20, message = "El ISBN no puede exceder 20 caracteres")
    @Schema(description = "Número ISBN del libro (único)", example = "9788497593793", maxLength = 20)
    private String isbn;

    @Size(max = 50, message = "La categoría no puede exceder 50 caracteres")
    @Schema(description = "Categoría o género del libro", example = "Ciencia Ficción", maxLength = 50)
    private String category;

    @Size(max = 100, message = "La editorial no puede exceder 100 caracteres")
    @Schema(description = "Nombre de la editorial", example = "Debolsillo", maxLength = 100)
    private String publisher;

    @Schema(description = "Año de publicación del libro", example = "1949", minimum = "1000", maximum = "2100")
    private Integer year;

    @Size(max = 2000, message = "La descripción no puede exceder 2000 caracteres")
    @Schema(description = "Descripción o sinopsis del libro", example = "Una distopía clásica que explora el totalitarismo y la vigilancia estatal.", maxLength = 2000)
    private String description;

    @Size(max = 500, message = "La URL de la portada no puede exceder 500 caracteres")
    @Schema(description = "URL de la imagen de portada del libro", example = "https://example.com/cover.jpg", maxLength = 500)
    private String coverUrl;

    @NotNull(message = "El número de copias es obligatorio")
    @Positive(message = "El número de copias debe ser positivo")
    @Schema(description = "Número total de copias del libro disponibles en la biblioteca", example = "5", requiredMode = Schema.RequiredMode.REQUIRED, minimum = "1")
    private Integer totalCopies;

    @Schema(description = "Precio del libro en formato decimal", example = "29.99", minimum = "0")
    private BigDecimal price;

    @Schema(description = "Indica si el libro está destacado en el catálogo", example = "true", defaultValue = "false")
    private Boolean featured;
}





