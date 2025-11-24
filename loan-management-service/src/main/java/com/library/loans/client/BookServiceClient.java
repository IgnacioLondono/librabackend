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
public class BookServiceClient {

    private final WebClient.Builder webClientBuilder;
    private final MicroservicesConfig microservicesConfig;

    public Mono<Boolean> checkBookAvailability(Long bookId) {
        String url = microservicesConfig.getBookCatalog().getUrl() + "/api/books/" + bookId + "/availability";

        return webClientBuilder.build()
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> {
                    Object available = response.get("available");
                    return available != null && Boolean.TRUE.equals(available);
                })
                .onErrorResume(error -> {
                    log.error("Error verificando disponibilidad del libro {}: {}", bookId, error.getMessage());
                    return Mono.just(false);
                });
    }

    public Mono<Void> updateBookCopies(Long bookId, Integer change) {
        String url = microservicesConfig.getBookCatalog().getUrl() + "/api/books/" + bookId + "/copies?change=" + change;

        return webClientBuilder.build()
                .patch()
                .uri(url)
                .retrieve()
                .bodyToMono(Void.class)
                .onErrorResume(error -> {
                    log.error("Error actualizando copias del libro {}: {}", bookId, error.getMessage());
                    return Mono.empty();
                });
    }
}





