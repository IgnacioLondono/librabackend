package com.library.notifications.dto;

import com.library.notifications.model.Notification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationResponseDTO {

    private Long id;
    private Long userId;
    private Notification.Type type;
    private String title;
    private String message;
    private Boolean read;
    private Notification.Priority priority;
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






