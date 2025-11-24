package com.library.books.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, length = 100)
    private String author;

    @Column(unique = true, length = 20)
    private String isbn;

    @Column(length = 50)
    private String category;

    @Column(length = 100)
    private String publisher;

    @Column(name = "publication_year")
    private Integer year;

    @Column(length = 2000)
    private String description;

    @Column(name = "cover_url", length = 500)
    private String coverUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private Status status = Status.AVAILABLE;

    @Column(name = "total_copies", nullable = false)
    @Builder.Default
    private Integer totalCopies = 1;

    @Column(name = "available_copies", nullable = false)
    @Builder.Default
    private Integer availableCopies = 1;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "featured")
    @Builder.Default
    private Boolean featured = false;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum Status {
        AVAILABLE, LOANED, RESERVED
    }

    public void updateAvailableCopies(int change) {
        this.availableCopies = Math.max(0, Math.min(this.availableCopies + change, this.totalCopies));
        updateStatus();
    }

    private void updateStatus() {
        if (this.availableCopies > 0) {
            this.status = Status.AVAILABLE;
        } else {
            this.status = Status.LOANED;
        }
    }
}






