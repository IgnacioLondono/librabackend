package com.library.loans.client;

import com.library.loans.config.MicroservicesConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceClient {

    private final WebClient.Builder webClientBuilder;
    private final MicroservicesConfig microservicesConfig;

    public Mono<Void> createNotification(Long userId, String type, String title, String message, String priority) {
        String url = microservicesConfig.getNotifications().getUrl() + "/api/notifications";

        Map<String, Object> notificationData = Map.of(
                "userId", userId,
                "type", type,
                "title", title,
                "message", message,
                "priority", priority != null ? priority : "MEDIUM"
        );

        return webClientBuilder.build()
                .post()
                .uri(url)
                .bodyValue(notificationData)
                .retrieve()
                .bodyToMono(Void.class)
                .onErrorResume(error -> {
                    log.error("Error creando notificación para usuario {}: {}", userId, error.getMessage());
                    return Mono.empty(); // No fallar el flujo principal si falla la notificación
                });
    }
}


