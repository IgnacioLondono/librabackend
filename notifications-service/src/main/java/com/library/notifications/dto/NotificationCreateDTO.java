package com.library.notifications.dto;

import com.library.notifications.model.Notification;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para crear una nueva notificación")
public class NotificationCreateDTO {

    @NotNull(message = "El ID del usuario es obligatorio")
    @Schema(description = "ID del usuario que recibirá la notificación", example = "5", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long userId;

    @NotNull(message = "El tipo de notificación es obligatorio")
    @Schema(description = "Tipo de notificación", example = "LOAN_CREATED",
            allowableValues = {"LOAN_CREATED", "LOAN_DUE", "LOAN_OVERDUE", "LOAN_RETURNED", "LOAN_EXTENDED", "LOAN_CANCELLED", "BOOK_AVAILABLE", "SYSTEM"},
            requiredMode = Schema.RequiredMode.REQUIRED)
    private Notification.Type type;

    @NotBlank(message = "El título es obligatorio")
    @Schema(description = "Título de la notificación", example = "Préstamo creado exitosamente", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 200)
    private String title;

    @NotBlank(message = "El mensaje es obligatorio")
    @Schema(description = "Mensaje completo de la notificación", example = "Tu préstamo del libro '1984' ha sido creado.", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 1000)
    private String message;

    @Schema(description = "Prioridad de la notificación (opcional, por defecto MEDIUM)", example = "MEDIUM",
            allowableValues = {"LOW", "MEDIUM", "HIGH"},
            defaultValue = "MEDIUM")
    private Notification.Priority priority;
}






