package com.library.books.dto;

import com.library.books.model.Book;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookResponseDTO {

    private Long id;
    private String title;
    private String author;
    private String isbn;
    private String category;
    private String publisher;
    private Integer year;
    private String description;
    private String coverUrl;
    private Book.Status status;
    private Integer totalCopies;
    private Integer availableCopies;
    private BigDecimal price;
    private Boolean featured;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static BookResponseDTO fromEntity(Book book) {
        return BookResponseDTO.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .isbn(book.getIsbn())
                .category(book.getCategory())
                .publisher(book.getPublisher())
                .year(book.getYear())
                .description(book.getDescription())
                .coverUrl(book.getCoverUrl())
                .status(book.getStatus())
                .totalCopies(book.getTotalCopies())
                .availableCopies(book.getAvailableCopies())
                .price(book.getPrice())
                .featured(book.getFeatured())
                .createdAt(book.getCreatedAt())
                .updatedAt(book.getUpdatedAt())
                .build();
    }
}






