package com.library.app.data.remote.api

import com.library.app.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface BookApiService {

    @POST("api/books")
    suspend fun createBook(@Body book: BookCreateDTO): Response<BookResponseDTO>

    @GET("api/books/{bookId}")
    suspend fun getBookById(@Path("bookId") bookId: Long): Response<BookResponseDTO>

    @GET("api/books")
    suspend fun getAllBooks(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10,
        @Query("sortBy") sortBy: String = "title",
        @Query("sortDir") sortDir: String = "ASC"
    ): Response<PageResponse<BookResponseDTO>>

    @GET("api/books/search")
    suspend fun searchBooks(
        @Query("q") query: String,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10
    ): Response<PageResponse<BookResponseDTO>>

    @GET("api/books/category/{category}")
    suspend fun getBooksByCategory(
        @Path("category") category: String,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10
    ): Response<PageResponse<BookResponseDTO>>

    @GET("api/books/featured")
    suspend fun getFeaturedBooks(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10
    ): Response<PageResponse<BookResponseDTO>>

    @PUT("api/books/{bookId}")
    suspend fun updateBook(
        @Path("bookId") bookId: Long,
        @Body update: BookUpdateDTO
    ): Response<BookResponseDTO>

    @DELETE("api/books/{bookId}")
    suspend fun deleteBook(@Path("bookId") bookId: Long): Response<Unit>

    @GET("api/books/{bookId}/availability")
    suspend fun checkAvailability(@Path("bookId") bookId: Long): Response<BookAvailabilityDTO>

    @PATCH("api/books/{bookId}/copies")
    suspend fun updateCopies(
        @Path("bookId") bookId: Long,
        @Query("change") change: Int
    ): Response<BookResponseDTO>

    @GET("api/books/statistics")
    suspend fun getStatistics(): Response<BookStatisticsDTO>

    @GET("api/books/all")
    suspend fun getAllBooksWithoutPagination(): Response<List<BookResponseDTO>>

    @POST("api/books/sync")
    suspend fun syncBooks(@Body books: List<BookSyncDTO>): Response<BookSyncResponseDTO>

    @POST("api/books/bulk")
    suspend fun createBooksBulk(@Body books: List<BookCreateDTO>): Response<List<BookResponseDTO>>

    @DELETE("api/books/bulk")
    suspend fun deleteBooksBulk(@Body bookIds: List<Long>): Response<Unit>
}

