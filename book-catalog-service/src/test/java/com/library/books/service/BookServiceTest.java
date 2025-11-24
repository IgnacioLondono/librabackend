package com.library.books.service;

import com.library.books.dto.BookCreateDTO;
import com.library.books.dto.BookResponseDTO;
import com.library.books.model.Book;
import com.library.books.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book testBook;
    private BookCreateDTO createDTO;

    @BeforeEach
    void setUp() {
        testBook = Book.builder()
                .id(1L)
                .title("Test Book")
                .author("Test Author")
                .isbn("1234567890")
                .category("Fiction")
                .totalCopies(5)
                .availableCopies(5)
                .price(new BigDecimal("29.99"))
                .build();

        createDTO = new BookCreateDTO();
        createDTO.setTitle("Test Book");
        createDTO.setAuthor("Test Author");
        createDTO.setIsbn("1234567890");
        createDTO.setCategory("Fiction");
        createDTO.setTotalCopies(5);
        createDTO.setPrice(new BigDecimal("29.99"));
    }

    @Test
    void testCreateBook_Success() {
        when(bookRepository.findByIsbn(anyString())).thenReturn(Optional.empty());
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);

        BookResponseDTO result = bookService.createBook(createDTO);

        assertNotNull(result);
        assertEquals(testBook.getTitle(), result.getTitle());
        assertEquals(testBook.getAuthor(), result.getAuthor());
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void testCreateBook_IsbnExists() {
        when(bookRepository.findByIsbn(anyString())).thenReturn(Optional.of(testBook));

        assertThrows(RuntimeException.class, () -> bookService.createBook(createDTO));
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void testGetBookById_Success() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));

        BookResponseDTO result = bookService.getBookById(1L);

        assertNotNull(result);
        assertEquals(testBook.getId(), result.getId());
    }

    @Test
    void testGetBookById_NotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> bookService.getBookById(1L));
    }
}


