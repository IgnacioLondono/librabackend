package com.library.books.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "DTO de respuesta para operaciones de sincronización masiva de libros desde Android")
public class BookSyncResponseDTO {

    @Schema(description = "Total de libros procesados", example = "10", requiredMode = Schema.RequiredMode.REQUIRED, minimum = "0")
    private Integer totalProcessed;

    @Schema(description = "Libros creados (nuevos)", example = "3", requiredMode = Schema.RequiredMode.REQUIRED, minimum = "0")
    private Integer created;

    @Schema(description = "Libros actualizados (ya existían)", example = "5", requiredMode = Schema.RequiredMode.REQUIRED, minimum = "0")
    private Integer updated;

    @Schema(description = "Libros omitidos (sin cambios)", example = "1", requiredMode = Schema.RequiredMode.REQUIRED, minimum = "0")
    private Integer skipped;

    @Schema(description = "Errores durante la sincronización", example = "1", requiredMode = Schema.RequiredMode.REQUIRED, minimum = "0")
    private Integer errors;

    @Schema(description = "Lista de libros creados exitosamente")
    private List<BookResponseDTO> createdBooks;

    @Schema(description = "Lista de libros actualizados exitosamente")
    private List<BookResponseDTO> updatedBooks;

    @Schema(description = "Lista de mensajes de error si hubo problemas")
    private List<String> errorMessages;
}


