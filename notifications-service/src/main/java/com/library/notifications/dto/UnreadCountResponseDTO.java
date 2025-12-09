package com.library.notifications.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO de respuesta con el conteo de notificaciones no leídas de un usuario")
public class UnreadCountResponseDTO {

    @Schema(description = "ID del usuario", example = "5", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long userId;

    @Schema(description = "Número de notificaciones no leídas", example = "3", requiredMode = Schema.RequiredMode.REQUIRED, minimum = "0")
    private Long unreadCount;
}


