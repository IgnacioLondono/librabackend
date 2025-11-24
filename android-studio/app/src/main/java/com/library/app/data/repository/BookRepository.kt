package com.library.app.data.repository

import com.library.app.data.remote.api.BookApiService
import com.library.app.data.remote.dto.*
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookRepository @Inject constructor(
    private val bookApiService: BookApiService
) {

    suspend fun getAllBooks(
        page: Int = 0,
        size: Int = 10,
        sortBy: String = "title",
        sortDir: String = "ASC"
    ): Response<PageResponse<BookResponseDTO>> {
        return bookApiService.getAllBooks(page, size, sortBy, sortDir)
    }

    suspend fun getBookById(bookId: Long): Response<BookResponseDTO> {
        return bookApiService.getBookById(bookId)
    }

    suspend fun searchBooks(
        query: String,
        page: Int = 0,
        size: Int = 10
    ): Response<PageResponse<BookResponseDTO>> {
        return bookApiService.searchBooks(query, page, size)
    }

    suspend fun getBooksByCategory(
        category: String,
        page: Int = 0,
        size: Int = 10
    ): Response<PageResponse<BookResponseDTO>> {
        return bookApiService.getBooksByCategory(category, page, size)
    }

    suspend fun getFeaturedBooks(
        page: Int = 0,
        size: Int = 10
    ): Response<PageResponse<BookResponseDTO>> {
        return bookApiService.getFeaturedBooks(page, size)
    }

    suspend fun checkAvailability(bookId: Long): Response<BookAvailabilityDTO> {
        return bookApiService.checkAvailability(bookId)
    }

    suspend fun getStatistics(): Response<BookStatisticsDTO> {
        return bookApiService.getStatistics()
    }

    suspend fun createBook(book: BookCreateDTO): Response<BookResponseDTO> {
        return bookApiService.createBook(book)
    }

    suspend fun updateBook(bookId: Long, update: BookUpdateDTO): Response<BookResponseDTO> {
        return bookApiService.updateBook(bookId, update)
    }

    suspend fun deleteBook(bookId: Long): Response<Unit> {
        return bookApiService.deleteBook(bookId)
    }
}


