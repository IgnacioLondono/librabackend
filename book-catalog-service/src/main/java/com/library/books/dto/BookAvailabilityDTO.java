package com.library.books.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO con información sobre la disponibilidad de un libro para préstamo")
public class BookAvailabilityDTO {

    @Schema(description = "ID del libro consultado", example = "1", required = true)
    private Long bookId;

    @Schema(description = "Indica si el libro está disponible para préstamo (tiene copias disponibles)", example = "true", required = true)
    private Boolean available;

    @Schema(description = "Número de copias disponibles para préstamo", example = "3", required = true)
    private Integer availableCopies;

    @Schema(description = "Número total de copias del libro en la biblioteca", example = "5", required = true)
    private Integer totalCopies;

    @Schema(description = "Mensaje descriptivo sobre la disponibilidad", example = "El libro está disponible", required = true)
    private String message;
}






