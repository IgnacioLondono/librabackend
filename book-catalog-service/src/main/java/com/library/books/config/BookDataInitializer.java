package com.library.books.config;

import com.library.books.repository.BookRepository;
import com.library.books.service.BookSeedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * CommandLineRunner para cargar datos iniciales automáticamente
 * Solo se ejecuta si la propiedad 'books.load-initial-data' está en 'true'
 * y la base de datos está vacía
 */
@Component
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(name = "books.load-initial-data", havingValue = "true", matchIfMissing = false)
public class BookDataInitializer implements CommandLineRunner {

    private final BookSeedService bookSeedService;
    private final BookRepository bookRepository;

    @Override
    public void run(String... args) throws Exception {
        long bookCount = bookRepository.count();
        
        if (bookCount == 0) {
            log.info("Base de datos vacía. Cargando libros precargados...");
            var result = bookSeedService.loadInitialBooks();
            log.info("Carga inicial completada: {}", result.getMessage());
        } else {
            log.info("Base de datos ya contiene {} libros. No se cargarán datos iniciales.", bookCount);
        }
    }
}


