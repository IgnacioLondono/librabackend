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
@Schema(description = "DTO con estadísticas del catálogo de libros")
public class BookStatisticsDTO {

    @Schema(description = "Total de libros únicos en el catálogo", example = "34", requiredMode = Schema.RequiredMode.REQUIRED, minimum = "0")
    private Long totalBooks;

    @Schema(description = "Libros con copias disponibles para préstamo", example = "28", requiredMode = Schema.RequiredMode.REQUIRED, minimum = "0")
    private Long availableBooks;

    @Schema(description = "Libros actualmente prestados", example = "5", requiredMode = Schema.RequiredMode.REQUIRED, minimum = "0")
    private Long loanedBooks;

    @Schema(description = "Libros reservados", example = "1", requiredMode = Schema.RequiredMode.REQUIRED, minimum = "0")
    private Long reservedBooks;

    @Schema(description = "Total de copias físicas de todos los libros", example = "250", requiredMode = Schema.RequiredMode.REQUIRED, minimum = "0")
    private Long totalCopies;

    @Schema(description = "Total de copias disponibles para préstamo", example = "200", requiredMode = Schema.RequiredMode.REQUIRED, minimum = "0")
    private Long availableCopies;
}


