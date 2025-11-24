package com.example.uinavegacion.data.repository

import com.example.uinavegacion.data.remote.dto.BookApi
import com.example.uinavegacion.data.remote.dto.BookDto
import com.example.uinavegacion.data.remote.dto.BookPageResponseDto

/**
 * Repository de ejemplo para Books
 * Este es un ejemplo de cómo estructurar Repositories para poder testearlos
 */
class BookRepository(
    private val bookApi: BookApi
) {
    
    suspend fun getBooks(page: Int = 0, size: Int = 10): List<BookDto> {
        val response = bookApi.getAllBooks(page, size, "title", "ASC")
        return if (response.isSuccessful) {
            response.body()?.content ?: emptyList()
        } else {
            emptyList()
        }
    }

    suspend fun searchBooks(query: String, page: Int = 0, size: Int = 10): List<BookDto> {
        val response = bookApi.searchBooks(query, page, size)
        return if (response.isSuccessful) {
            response.body()?.content ?: emptyList()
        } else {
            emptyList()
        }
    }

    suspend fun getBookById(bookId: Long): BookDto? {
        val response = bookApi.getBook(bookId)
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }

    suspend fun checkAvailability(bookId: Long): Boolean {
        val response = bookApi.checkAvailability(bookId)
        return if (response.isSuccessful) {
            response.body()?.available ?: false
        } else {
            false
        }
    }
}


