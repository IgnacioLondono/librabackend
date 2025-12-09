package com.library.notifications.dto;

import com.library.notifications.model.Notification;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO de respuesta con la información completa de una notificación")
public class NotificationResponseDTO {

    @Schema(description = "ID único de la notificación generado automáticamente", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Schema(description = "ID del usuario que recibe la notificación", example = "5", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long userId;

    @Schema(description = "Tipo de notificación", example = "LOAN_CREATED", 
            allowableValues = {"LOAN_CREATED", "LOAN_DUE", "LOAN_OVERDUE", "LOAN_RETURNED", "LOAN_EXTENDED", "LOAN_CANCELLED", "BOOK_AVAILABLE", "SYSTEM"},
            requiredMode = Schema.RequiredMode.REQUIRED)
    private Notification.Type type;

    @Schema(description = "Título de la notificación", example = "Préstamo creado exitosamente", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    @Schema(description = "Mensaje completo de la notificación", example = "Tu préstamo del libro '1984' ha sido creado. Fecha de devolución: 2024-01-29", requiredMode = Schema.RequiredMode.REQUIRED)
    private String message;

    @Schema(description = "Indica si la notificación ha sido leída", example = "false", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean read;

    @Schema(description = "Prioridad de la notificación", example = "MEDIUM", 
            allowableValues = {"LOW", "MEDIUM", "HIGH"},
            requiredMode = Schema.RequiredMode.REQUIRED)
    private Notification.Priority priority;

    @Schema(description = "Fecha y hora de creación de la notificación", example = "2024-01-15T10:30:00", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createdAt;

    public static NotificationResponseDTO fromEntity(Notification notification) {
        return NotificationResponseDTO.builder()
                .id(notification.getId())
                .userId(notification.getUserId())
                .type(notification.getType())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .read(notification.getRead())
                .priority(notification.getPriority())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}






