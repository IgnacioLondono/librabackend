package com.library.books;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BookCatalogServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookCatalogServiceApplication.class, args);
    }
}






