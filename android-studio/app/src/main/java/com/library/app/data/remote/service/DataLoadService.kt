package com.library.app.data.remote.service

import com.library.app.data.remote.api.BookApiService
import com.library.app.data.remote.api.UserApiService
import com.library.app.data.remote.dto.BookCreateDTO
import com.library.app.data.remote.dto.BookDTOs
import com.library.app.data.remote.dto.UserDTOs
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Servicio para cargar datos iniciales desde JSON a los microservicios
 * 
 * Este servicio lee el archivo seed-data-for-microservices.json desde assets
 * y carga los libros y usuarios en los microservicios correspondientes.
 */
@Singleton
class DataLoadService @Inject constructor(
    private val bookApiService: BookApiService,
    private val userApiService: UserApiService,
    private val gson: Gson
) {
    
    data class SeedData(
        val books: List<BookCreateDTO>,
        val users: List<UserDTOs.UserRegistrationRequest>
    )
    
    /**
     * Carga todos los datos desde el archivo JSON en assets
     * @param inputStream InputStream del archivo JSON desde assets
     * @return Resultado de la carga con estadísticas
     */
    suspend fun loadAllData(inputStream: InputStream): LoadResult = withContext(Dispatchers.IO) {
        try {
            // Leer y parsear JSON
            val jsonString = inputStream.bufferedReader().use { it.readText() }
            val seedData = parseJson(jsonString)
            
            // Cargar usuarios primero
            val userResult = loadUsers(seedData.users)
            
            // Cargar libros después
            val bookResult = loadBooks(seedData.books)
            
            LoadResult(
                usersLoaded = userResult.inserted,
                usersSkipped = userResult.alreadyExists,
                booksLoaded = bookResult.inserted,
                booksSkipped = bookResult.alreadyExists,
                success = true,
                message = "Datos cargados: ${userResult.inserted} usuarios, ${bookResult.inserted} libros"
            )
        } catch (e: Exception) {
            LoadResult(
                usersLoaded = 0,
                usersSkipped = 0,
                booksLoaded = 0,
                booksSkipped = 0,
                success = false,
                message = "Error cargando datos: ${e.message}"
            )
        }
    }
    
    /**
     * Carga solo los usuarios
     */
    suspend fun loadUsers(users: List<UserDTOs.UserRegistrationRequest>): BulkLoadResponse {
        return try {
            val response = userApiService.loadUsersBulk(users)
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                BulkLoadResponse(
                    inserted = body.inserted,
                    alreadyExists = body.alreadyExists,
                    errors = body.errors
                )
            } else {
                BulkLoadResponse(0, 0, 1)
            }
        } catch (e: Exception) {
            BulkLoadResponse(0, 0, 1)
        }
    }
    
    /**
     * Carga solo los libros
     */
    suspend fun loadBooks(books: List<BookCreateDTO>): BulkLoadResponse {
        return try {
            val response = bookApiService.loadBooksBulk(books)
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                BulkLoadResponse(
                    inserted = body.inserted,
                    alreadyExists = body.alreadyExists,
                    errors = body.errors
                )
            } else {
                BulkLoadResponse(0, 0, 1)
            }
        } catch (e: Exception) {
            BulkLoadResponse(0, 0, 1)
        }
    }
    
    /**
     * Parsea el JSON a objetos Kotlin
     */
    private fun parseJson(jsonString: String): SeedData {
        val type = object : TypeToken<Map<String, Any>>() {}.type
        val jsonMap: Map<String, Any> = gson.fromJson(jsonString, type)
        
        // Parsear libros
        val booksJson = jsonMap["books"] as? List<Map<String, Any>> ?: emptyList()
        val books = booksJson.map { bookMap ->
            BookCreateDTO(
                title = bookMap["title"] as? String ?: "",
                author = bookMap["author"] as? String ?: "",
                isbn = bookMap["isbn"] as? String,
                category = bookMap["categoria"] as? String,
                publisher = bookMap["publisher"] as? String,
                year = (bookMap["anio"] as? Number)?.toInt(),
                description = bookMap["descripcion"] as? String,
                coverUrl = bookMap["coverUrl"] as? String,
                totalCopies = (bookMap["stock"] as? Number)?.toInt() ?: 1,
                price = null,
                featured = (bookMap["homeSection"] as? String)?.let { 
                    it == "Trending" || it == "Free" 
                } ?: false
            )
        }
        
        // Parsear usuarios
        val usersJson = jsonMap["users"] as? List<Map<String, Any>> ?: emptyList()
        val users = usersJson.map { userMap ->
            UserDTOs.UserRegistrationRequest(
                name = userMap["name"] as? String ?: "",
                email = userMap["email"] as? String ?: "",
                phone = userMap["phone"] as? String,
                password = userMap["password"] as? String ?: ""
            )
        }
        
        return SeedData(books, users)
    }
    
    data class LoadResult(
        val usersLoaded: Int,
        val usersSkipped: Int,
        val booksLoaded: Int,
        val booksSkipped: Int,
        val success: Boolean,
        val message: String
    )
    
    data class BulkLoadResponse(
        val inserted: Int,
        val alreadyExists: Int,
        val errors: Int
    )
}

