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

    @Schema(description = "ID del libro consultado", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long bookId;

    @Schema(description = "Indica si el libro está disponible para préstamo (tiene copias disponibles)", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean available;

    @Schema(description = "Número de copias disponibles para préstamo", example = "3", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer availableCopies;

    @Schema(description = "Número total de copias del libro en la biblioteca", example = "5", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer totalCopies;

    @Schema(description = "Mensaje descriptivo sobre la disponibilidad", example = "El libro está disponible", requiredMode = Schema.RequiredMode.REQUIRED)
    private String message;
}






