package com.library.notifications.service;

import com.library.notifications.dto.NotificationCreateDTO;
import com.library.notifications.dto.NotificationResponseDTO;
import com.library.notifications.model.Notification;
import com.library.notifications.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Transactional
    public NotificationResponseDTO createNotification(NotificationCreateDTO createDTO) {
        log.info("Creando notificación para usuario: {}", createDTO.getUserId());

        Notification notification = Notification.builder()
                .userId(createDTO.getUserId())
                .type(createDTO.getType())
                .title(createDTO.getTitle())
                .message(createDTO.getMessage())
                .priority(createDTO.getPriority() != null ? createDTO.getPriority() : Notification.Priority.MEDIUM)
                .read(false)
                .build();

        notification = notificationRepository.save(notification);
        return NotificationResponseDTO.fromEntity(notification);
    }

    public List<NotificationResponseDTO> getUserNotifications(Long userId, Boolean unreadOnly) {
        List<Notification> notifications;
        if (unreadOnly != null && unreadOnly) {
            notifications = notificationRepository.findByUserIdAndReadFalseOrderByCreatedAtDesc(userId);
        } else {
            notifications = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
        }
        return notifications.stream()
                .map(NotificationResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public NotificationResponseDTO markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada"));
        notification.setRead(true);
        notification = notificationRepository.save(notification);
        return NotificationResponseDTO.fromEntity(notification);
    }

    @Transactional
    public void markAllAsRead(Long userId) {
        List<Notification> notifications = notificationRepository.findByUserIdAndReadFalseOrderByCreatedAtDesc(userId);
        notifications.forEach(n -> n.setRead(true));
        notificationRepository.saveAll(notifications);
    }

    @Transactional
    public void deleteNotification(Long notificationId) {
        notificationRepository.deleteById(notificationId);
    }

    public Long getUnreadCount(Long userId) {
        return notificationRepository.countByUserIdAndReadFalse(userId);
    }
}






