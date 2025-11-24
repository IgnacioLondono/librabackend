package com.library.notifications.service;

import com.library.notifications.dto.NotificationCreateDTO;
import com.library.notifications.dto.NotificationResponseDTO;
import com.library.notifications.model.Notification;
import com.library.notifications.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationService notificationService;

    private Notification testNotification;
    private NotificationCreateDTO createDTO;

    @BeforeEach
    void setUp() {
        testNotification = Notification.builder()
                .id(1L)
                .userId(1L)
                .type(Notification.Type.SYSTEM)
                .title("Test Notification")
                .message("Test Message")
                .priority(Notification.Priority.MEDIUM)
                .read(false)
                .build();

        createDTO = new NotificationCreateDTO();
        createDTO.setUserId(1L);
        createDTO.setType(Notification.Type.SYSTEM);
        createDTO.setTitle("Test Notification");
        createDTO.setMessage("Test Message");
        createDTO.setPriority(Notification.Priority.MEDIUM);
    }

    @Test
    void testCreateNotification_Success() {
        when(notificationRepository.save(any(Notification.class))).thenReturn(testNotification);

        NotificationResponseDTO result = notificationService.createNotification(createDTO);

        assertNotNull(result);
        assertEquals(testNotification.getTitle(), result.getTitle());
        assertEquals(testNotification.getMessage(), result.getMessage());
        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    @Test
    void testGetUserNotifications_Success() {
        List<Notification> notifications = new ArrayList<>();
        notifications.add(testNotification);
        
        when(notificationRepository.findByUserIdOrderByCreatedAtDesc(1L)).thenReturn(notifications);

        List<NotificationResponseDTO> result = notificationService.getUserNotifications(1L, false);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testMarkAsRead_Success() {
        when(notificationRepository.findById(1L)).thenReturn(Optional.of(testNotification));
        when(notificationRepository.save(any(Notification.class))).thenReturn(testNotification);

        NotificationResponseDTO result = notificationService.markAsRead(1L);

        assertNotNull(result);
        assertTrue(testNotification.getRead());
        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    @Test
    void testMarkAsRead_NotFound() {
        when(notificationRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> notificationService.markAsRead(1L));
    }

    @Test
    void testGetUnreadCount_Success() {
        when(notificationRepository.countByUserIdAndReadFalse(1L)).thenReturn(5L);

        Long result = notificationService.getUnreadCount(1L);

        assertNotNull(result);
        assertEquals(5L, result);
    }
}


