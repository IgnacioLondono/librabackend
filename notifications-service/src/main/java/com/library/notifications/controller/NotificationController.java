package com.library.notifications.controller;

import com.library.notifications.dto.NotificationCreateDTO;
import com.library.notifications.dto.NotificationResponseDTO;
import com.library.notifications.dto.UnreadCountResponseDTO;
import com.library.notifications.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(name = "Notifications", description = "API para gestión de notificaciones")
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    @Operation(summary = "Crear notificación", description = "Crea una nueva notificación")
    public ResponseEntity<NotificationResponseDTO> createNotification(
            @Valid @RequestBody NotificationCreateDTO createDTO) {
        NotificationResponseDTO notification = notificationService.createNotification(createDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(notification);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Notificaciones de usuario", description = "Obtiene las notificaciones de un usuario")
    public ResponseEntity<List<NotificationResponseDTO>> getUserNotifications(
            @Parameter(description = "ID del usuario") @PathVariable Long userId,
            @Parameter(description = "Solo no leídas") @RequestParam(required = false) Boolean unreadOnly) {
        List<NotificationResponseDTO> notifications = notificationService.getUserNotifications(userId, unreadOnly);
        return ResponseEntity.ok(notifications);
    }

    @PatchMapping("/{notificationId}/read")
    @Operation(summary = "Marcar como leída", description = "Marca una notificación como leída")
    public ResponseEntity<NotificationResponseDTO> markAsRead(
            @Parameter(description = "ID de la notificación") @PathVariable Long notificationId) {
        NotificationResponseDTO notification = notificationService.markAsRead(notificationId);
        return ResponseEntity.ok(notification);
    }

    @PatchMapping("/user/{userId}/read-all")
    @Operation(summary = "Marcar todas como leídas", description = "Marca todas las notificaciones de un usuario como leídas")
    public ResponseEntity<Void> markAllAsRead(
            @Parameter(description = "ID del usuario") @PathVariable Long userId) {
        notificationService.markAllAsRead(userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{notificationId}")
    @Operation(summary = "Eliminar notificación", description = "Elimina una notificación")
    public ResponseEntity<Void> deleteNotification(
            @Parameter(description = "ID de la notificación") @PathVariable Long notificationId) {
        notificationService.deleteNotification(notificationId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}/unread-count")
    @Operation(summary = "Contador de no leídas", description = "Obtiene el número de notificaciones no leídas de un usuario")
    public ResponseEntity<UnreadCountResponseDTO> getUnreadCount(
            @Parameter(description = "ID del usuario") @PathVariable Long userId) {
        Long count = notificationService.getUnreadCount(userId);
        UnreadCountResponseDTO response = UnreadCountResponseDTO.builder()
                .userId(userId)
                .unreadCount(count)
                .build();
        return ResponseEntity.ok(response);
    }
}





