package com.library.books.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookUpdateDTO {

    @Size(max = 200, message = "El título no puede exceder 200 caracteres")
    private String title;

    @Size(max = 100, message = "El autor no puede exceder 100 caracteres")
    private String author;

    @Size(max = 20, message = "El ISBN no puede exceder 20 caracteres")
    private String isbn;

    @Size(max = 50, message = "La categoría no puede exceder 50 caracteres")
    private String category;

    @Size(max = 100, message = "La editorial no puede exceder 100 caracteres")
    private String publisher;

    private Integer year;

    @Size(max = 2000, message = "La descripción no puede exceder 2000 caracteres")
    private String description;

    @Size(max = 500, message = "La URL de la portada no puede exceder 500 caracteres")
    private String coverUrl;

    @Positive(message = "El número de copias debe ser positivo")
    private Integer totalCopies;

    private BigDecimal price;

    private Boolean featured;
}






