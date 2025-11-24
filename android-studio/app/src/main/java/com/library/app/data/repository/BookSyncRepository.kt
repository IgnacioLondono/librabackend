package com.library.app.data.repository

import com.library.app.data.remote.api.BookApiService
import com.library.app.data.remote.dto.*
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repositorio para operaciones de sincronización de libros
 */
@Singleton
class BookSyncRepository @Inject constructor(
    private val bookApiService: BookApiService
) {

    /**
     * Obtener todos los libros del servidor (sin paginación)
     * Útil para sincronización inicial
     */
    suspend fun getAllBooksFromServer(): Response<List<BookResponseDTO>> {
        return bookApiService.getAllBooksWithoutPagination()
    }

    /**
     * Sincronizar libros desde Android al servidor
     * Crea nuevos o actualiza existentes basándose en ISBN o título+autor
     */
    suspend fun syncBooksToServer(books: List<BookSyncDTO>): Response<BookSyncResponseDTO> {
        return bookApiService.syncBooks(books)
    }

    /**
     * Crear múltiples libros en el servidor
     */
    suspend fun createBooksBulk(books: List<BookCreateDTO>): Response<List<BookResponseDTO>> {
        return bookApiService.createBooksBulk(books)
    }

    /**
     * Eliminar múltiples libros del servidor
     */
    suspend fun deleteBooksBulk(bookIds: List<Long>): Response<Unit> {
        return bookApiService.deleteBooksBulk(bookIds)
    }
}


