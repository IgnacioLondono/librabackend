package com.library.books.service;

import com.library.books.dto.*;
import com.library.books.model.Book;
import com.library.books.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookService {

    private final BookRepository bookRepository;

    @Transactional
    public BookResponseDTO createBook(BookCreateDTO createDTO) {
        log.info("Creando nuevo libro: {}", createDTO.getTitle());

        // Validar campos obligatorios
        if (createDTO.getTitle() == null || createDTO.getTitle().trim().isEmpty()) {
            throw new RuntimeException("El título del libro es obligatorio");
        }
        if (createDTO.getAuthor() == null || createDTO.getAuthor().trim().isEmpty()) {
            throw new RuntimeException("El autor del libro es obligatorio");
        }

        // Validar ISBN único si se proporciona
        if (createDTO.getIsbn() != null && !createDTO.getIsbn().trim().isEmpty()) {
            bookRepository.findByIsbn(createDTO.getIsbn())
                    .ifPresent(book -> {
                        throw new RuntimeException("Ya existe un libro con el ISBN: " + createDTO.getIsbn());
                    });
        }

        // Validar copias
        int totalCopies = createDTO.getTotalCopies() != null ? createDTO.getTotalCopies() : 1;
        if (totalCopies < 1) {
            throw new RuntimeException("El número de copias debe ser al menos 1");
        }

        Book book = Book.builder()
                .title(createDTO.getTitle().trim())
                .author(createDTO.getAuthor().trim())
                .isbn(createDTO.getIsbn() != null ? createDTO.getIsbn().trim() : null)
                .category(createDTO.getCategory() != null ? createDTO.getCategory().trim() : null)
                .publisher(createDTO.getPublisher() != null ? createDTO.getPublisher().trim() : null)
                .year(createDTO.getYear())
                .description(createDTO.getDescription() != null ? createDTO.getDescription().trim() : null)
                .coverUrl(createDTO.getCoverUrl() != null ? createDTO.getCoverUrl().trim() : null)
                .totalCopies(totalCopies)
                .availableCopies(totalCopies)
                .price(createDTO.getPrice())
                .featured(createDTO.getFeatured() != null ? createDTO.getFeatured() : false)
                .build();

        book = bookRepository.save(book);
        log.info("Libro creado exitosamente con ID: {} - Título: {}", book.getId(), book.getTitle());

        return BookResponseDTO.fromEntity(book);
    }

    public BookResponseDTO getBookById(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado"));
        return BookResponseDTO.fromEntity(book);
    }

    public Page<BookResponseDTO> getAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .map(BookResponseDTO::fromEntity);
    }

    public Page<BookResponseDTO> searchBooks(String query, Pageable pageable) {
        return bookRepository.searchBooks(query, pageable)
                .map(BookResponseDTO::fromEntity);
    }

    public Page<BookResponseDTO> getBooksByCategory(String category, Pageable pageable) {
        return bookRepository.findByCategory(category, pageable)
                .map(BookResponseDTO::fromEntity);
    }

    public Page<BookResponseDTO> getFeaturedBooks(Pageable pageable) {
        return bookRepository.findByFeaturedTrue(pageable)
                .map(BookResponseDTO::fromEntity);
    }

    @Transactional
    public BookResponseDTO updateBook(Long bookId, BookUpdateDTO updateDTO) {
        log.info("Actualizando libro: {}", bookId);

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado"));

        if (updateDTO.getTitle() != null && !updateDTO.getTitle().trim().isEmpty()) {
            book.setTitle(updateDTO.getTitle().trim());
        }
        if (updateDTO.getAuthor() != null && !updateDTO.getAuthor().trim().isEmpty()) {
            book.setAuthor(updateDTO.getAuthor().trim());
        }
        if (updateDTO.getIsbn() != null && !updateDTO.getIsbn().trim().isEmpty()) {
            String newIsbn = updateDTO.getIsbn().trim();
            if (!newIsbn.equals(book.getIsbn())) {
                bookRepository.findByIsbn(newIsbn)
                        .ifPresent(b -> {
                            throw new RuntimeException("Ya existe un libro con el ISBN: " + newIsbn);
                        });
            }
            book.setIsbn(newIsbn);
        }
        if (updateDTO.getCategory() != null) {
            book.setCategory(updateDTO.getCategory().trim());
        }
        if (updateDTO.getPublisher() != null) {
            book.setPublisher(updateDTO.getPublisher().trim());
        }
        if (updateDTO.getYear() != null) {
            book.setYear(updateDTO.getYear());
        }
        if (updateDTO.getDescription() != null) {
            book.setDescription(updateDTO.getDescription().trim());
        }
        if (updateDTO.getCoverUrl() != null) {
            book.setCoverUrl(updateDTO.getCoverUrl().trim());
        }
        if (updateDTO.getTotalCopies() != null) {
            int newTotalCopies = updateDTO.getTotalCopies();
            if (newTotalCopies < 1) {
                throw new RuntimeException("El número de copias debe ser al menos 1");
            }
            int difference = newTotalCopies - book.getTotalCopies();
            book.setTotalCopies(newTotalCopies);
            // Ajustar copias disponibles manteniendo la diferencia
            int newAvailableCopies = book.getAvailableCopies() + difference;
            if (newAvailableCopies < 0) {
                newAvailableCopies = 0;
            }
            book.setAvailableCopies(newAvailableCopies);
        }
        if (updateDTO.getPrice() != null) {
            if (updateDTO.getPrice().compareTo(java.math.BigDecimal.ZERO) < 0) {
                throw new RuntimeException("El precio no puede ser negativo");
            }
            book.setPrice(updateDTO.getPrice());
        }
        if (updateDTO.getFeatured() != null) {
            book.setFeatured(updateDTO.getFeatured());
        }

        book = bookRepository.save(book);
        log.info("Libro {} actualizado exitosamente", bookId);
        return BookResponseDTO.fromEntity(book);
    }

    @Transactional
    public void deleteBook(Long bookId) {
        log.info("Eliminando libro: {}", bookId);
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado"));
        
        // Validar que no tenga copias prestadas (indicador de préstamos activos)
        // NOTA: Para una validación completa, se debería consultar el servicio de préstamos
        // para verificar préstamos activos. Por ahora, validamos que todas las copias estén disponibles.
        if (book.getAvailableCopies() < book.getTotalCopies()) {
            throw new RuntimeException(
                String.format("No se puede eliminar el libro porque tiene %d copias prestadas. " +
                             "Debe devolver todos los préstamos activos antes de eliminar el libro.",
                             book.getTotalCopies() - book.getAvailableCopies())
            );
        }
        
        bookRepository.delete(book);
        log.info("Libro {} eliminado exitosamente", bookId);
    }

    public BookAvailabilityDTO checkAvailability(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado"));

        boolean available = book.getAvailableCopies() > 0;

        return BookAvailabilityDTO.builder()
                .bookId(bookId)
                .available(available)
                .availableCopies(book.getAvailableCopies())
                .totalCopies(book.getTotalCopies())
                .message(available
                        ? "El libro está disponible"
                        : "El libro no está disponible")
                .build();
    }

    @Transactional
    public BookResponseDTO updateCopies(Long bookId, Integer copiesChange) {
        log.info("Actualizando copias del libro {}: {}", bookId, copiesChange);

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado"));

        int previousAvailableCopies = book.getAvailableCopies();
        book.updateAvailableCopies(copiesChange);
        book = bookRepository.save(book);

        // Si el libro estaba sin copias disponibles y ahora tiene, podría notificar
        // (esto se manejaría desde el servicio de préstamos cuando se devuelve un libro)
        if (previousAvailableCopies == 0 && book.getAvailableCopies() > 0) {
            log.info("Libro {} ahora tiene {} copias disponibles", bookId, book.getAvailableCopies());
        }

        return BookResponseDTO.fromEntity(book);
    }

    /**
     * Validar que el libro existe y está disponible
     */
    public boolean isBookAvailable(Long bookId) {
        try {
            Book book = bookRepository.findById(bookId)
                    .orElseThrow(() -> new RuntimeException("Libro no encontrado"));
            return book.getAvailableCopies() > 0;
        } catch (Exception e) {
            log.error("Error verificando disponibilidad del libro {}: {}", bookId, e.getMessage());
            return false;
        }
    }

    /**
     * Obtener estadísticas del catálogo de libros
     */
    public BookStatisticsDTO getBookStatistics() {
        long totalBooks = bookRepository.count();
        long availableBooks = bookRepository.countByAvailableCopiesGreaterThan(0);
        long totalCopies = bookRepository.findAll().stream()
                .mapToLong(Book::getTotalCopies)
                .sum();
        long availableCopies = bookRepository.findAll().stream()
                .mapToLong(Book::getAvailableCopies)
                .sum();
        long loanedBooks = totalBooks - availableBooks;

        return BookStatisticsDTO.builder()
                .totalBooks(totalBooks)
                .availableBooks(availableBooks)
                .loanedBooks(loanedBooks)
                .reservedBooks(0L) // Por ahora no hay reservas
                .totalCopies(totalCopies)
                .availableCopies(availableCopies)
                .build();
    }

    /**
     * Obtener todos los libros sin paginación (para sincronización)
     */
    public List<BookResponseDTO> getAllBooksWithoutPagination() {
        return bookRepository.findAll().stream()
                .map(BookResponseDTO::fromEntity)
                .toList();
    }

    /**
     * Sincronización masiva de libros desde Android
     * Crea nuevos libros o actualiza existentes basándose en ISBN
     */
    @Transactional
    public BookSyncResponseDTO syncBooks(List<BookSyncDTO> booksToSync) {
        log.info("Iniciando sincronización de {} libros", booksToSync.size());

        int created = 0;
        int updated = 0;
        int skipped = 0;
        int errors = 0;
        List<BookResponseDTO> createdBooks = new java.util.ArrayList<>();
        List<BookResponseDTO> updatedBooks = new java.util.ArrayList<>();
        List<String> errorMessages = new java.util.ArrayList<>();

        for (BookSyncDTO syncDTO : booksToSync) {
            try {
                // Validar campos obligatorios
                if (syncDTO.getTitle() == null || syncDTO.getTitle().trim().isEmpty()) {
                    skipped++;
                    errorMessages.add("Libro sin título - ISBN: " + syncDTO.getIsbn());
                    continue;
                }
                if (syncDTO.getAuthor() == null || syncDTO.getAuthor().trim().isEmpty()) {
                    skipped++;
                    errorMessages.add("Libro sin autor - Título: " + syncDTO.getTitle());
                    continue;
                }

                Book existingBook = null;

                // Buscar por ISBN si está disponible
                if (syncDTO.getIsbn() != null && !syncDTO.getIsbn().trim().isEmpty()) {
                    existingBook = bookRepository.findByIsbn(syncDTO.getIsbn().trim())
                            .orElse(null);
                }

                // Si no se encontró por ISBN, buscar por título y autor
                if (existingBook == null) {
                    existingBook = bookRepository.findAll().stream()
                            .filter(b -> b.getTitle().equalsIgnoreCase(syncDTO.getTitle().trim()) &&
                                    b.getAuthor().equalsIgnoreCase(syncDTO.getAuthor().trim()))
                            .findFirst()
                            .orElse(null);
                }

                if (existingBook != null) {
                    // Actualizar libro existente
                    updateBookFromSync(existingBook, syncDTO);
                    existingBook = bookRepository.save(existingBook);
                    updated++;
                    updatedBooks.add(BookResponseDTO.fromEntity(existingBook));
                    log.debug("Libro actualizado: {} - {}", existingBook.getId(), existingBook.getTitle());
                } else {
                    // Crear nuevo libro
                    Book newBook = createBookFromSync(syncDTO);
                    newBook = bookRepository.save(newBook);
                    created++;
                    createdBooks.add(BookResponseDTO.fromEntity(newBook));
                    log.debug("Libro creado: {} - {}", newBook.getId(), newBook.getTitle());
                }
            } catch (Exception e) {
                errors++;
                String errorMsg = String.format("Error procesando libro '%s' por %s: %s",
                        syncDTO.getTitle(), syncDTO.getAuthor(), e.getMessage());
                errorMessages.add(errorMsg);
                log.error(errorMsg, e);
            }
        }

        log.info("Sincronización completada: {} creados, {} actualizados, {} omitidos, {} errores",
                created, updated, skipped, errors);

        return BookSyncResponseDTO.builder()
                .totalProcessed(booksToSync.size())
                .created(created)
                .updated(updated)
                .skipped(skipped)
                .errors(errors)
                .createdBooks(createdBooks)
                .updatedBooks(updatedBooks)
                .errorMessages(errorMessages)
                .build();
    }

    /**
     * Crear libro desde DTO de sincronización
     */
    private Book createBookFromSync(BookSyncDTO syncDTO) {
        int totalCopies = syncDTO.getTotalCopies() != null ? syncDTO.getTotalCopies() : 1;
        int availableCopies = syncDTO.getAvailableCopies() != null 
                ? syncDTO.getAvailableCopies() 
                : totalCopies;

        return Book.builder()
                .title(syncDTO.getTitle().trim())
                .author(syncDTO.getAuthor().trim())
                .isbn(syncDTO.getIsbn() != null ? syncDTO.getIsbn().trim() : null)
                .category(syncDTO.getCategory() != null ? syncDTO.getCategory().trim() : null)
                .publisher(syncDTO.getPublisher() != null ? syncDTO.getPublisher().trim() : null)
                .year(syncDTO.getYear())
                .description(syncDTO.getDescription() != null ? syncDTO.getDescription().trim() : null)
                .coverUrl(syncDTO.getCoverUrl() != null ? syncDTO.getCoverUrl().trim() : null)
                .totalCopies(totalCopies)
                .availableCopies(availableCopies)
                .price(syncDTO.getPrice())
                .featured(syncDTO.getFeatured() != null ? syncDTO.getFeatured() : false)
                .build();
    }

    /**
     * Actualizar libro existente desde DTO de sincronización
     */
    private void updateBookFromSync(Book existingBook, BookSyncDTO syncDTO) {
        if (syncDTO.getTitle() != null && !syncDTO.getTitle().trim().isEmpty()) {
            existingBook.setTitle(syncDTO.getTitle().trim());
        }
        if (syncDTO.getAuthor() != null && !syncDTO.getAuthor().trim().isEmpty()) {
            existingBook.setAuthor(syncDTO.getAuthor().trim());
        }
        if (syncDTO.getIsbn() != null && !syncDTO.getIsbn().trim().isEmpty()) {
            // Solo actualizar ISBN si no está duplicado
            String newIsbn = syncDTO.getIsbn().trim();
            if (!newIsbn.equals(existingBook.getIsbn())) {
                bookRepository.findByIsbn(newIsbn)
                        .ifPresentOrElse(
                                b -> log.warn("ISBN {} ya existe, no se actualizará", newIsbn),
                                () -> existingBook.setIsbn(newIsbn)
                        );
            }
        }
        if (syncDTO.getCategory() != null) {
            existingBook.setCategory(syncDTO.getCategory().trim());
        }
        if (syncDTO.getPublisher() != null) {
            existingBook.setPublisher(syncDTO.getPublisher().trim());
        }
        if (syncDTO.getYear() != null) {
            existingBook.setYear(syncDTO.getYear());
        }
        if (syncDTO.getDescription() != null) {
            existingBook.setDescription(syncDTO.getDescription().trim());
        }
        if (syncDTO.getCoverUrl() != null) {
            existingBook.setCoverUrl(syncDTO.getCoverUrl().trim());
        }
        if (syncDTO.getTotalCopies() != null) {
            int newTotalCopies = syncDTO.getTotalCopies();
            int difference = newTotalCopies - existingBook.getTotalCopies();
            existingBook.setTotalCopies(newTotalCopies);
            // Ajustar copias disponibles
            if (syncDTO.getAvailableCopies() != null) {
                existingBook.setAvailableCopies(syncDTO.getAvailableCopies());
            } else {
                int newAvailableCopies = existingBook.getAvailableCopies() + difference;
                existingBook.setAvailableCopies(Math.max(0, newAvailableCopies));
            }
        } else if (syncDTO.getAvailableCopies() != null) {
            existingBook.setAvailableCopies(syncDTO.getAvailableCopies());
        }
        if (syncDTO.getPrice() != null) {
            existingBook.setPrice(syncDTO.getPrice());
        }
        if (syncDTO.getFeatured() != null) {
            existingBook.setFeatured(syncDTO.getFeatured());
        }
    }

    /**
     * Eliminar múltiples libros por IDs
     */
    @Transactional
    public void deleteBooks(List<Long> bookIds) {
        log.info("Eliminando {} libros", bookIds.size());
        for (Long bookId : bookIds) {
            try {
                deleteBook(bookId);
            } catch (Exception e) {
                log.error("Error eliminando libro {}: {}", bookId, e.getMessage());
            }
        }
    }
}





