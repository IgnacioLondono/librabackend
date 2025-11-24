# 🌐 Integración con API Externa

## Requisito: Consumir API Externa

Debes integrar una API externa (Google Books API o Open Library API) que se muestre en la interfaz sin interferir con los datos locales ni microservicios propios.

## 📚 Opción 1: Google Books API

### Configuración

**1. Agregar API en `data/remote/dto/GoogleBooksApi.kt`:**

```kotlin
package com.example.uinavegacion.data.remote.dto

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleBooksApi {
    @GET("volumes")
    suspend fun searchBooks(
        @Query("q") query: String,
        @Query("maxResults") maxResults: Int = 10
    ): Response<GoogleBooksResponse>
}

data class GoogleBooksResponse(
    val items: List<GoogleBookItem>?
)

data class GoogleBookItem(
    val id: String,
    val volumeInfo: VolumeInfo
)

data class VolumeInfo(
    val title: String,
    val authors: List<String>?,
    val description: String?,
    val imageLinks: ImageLinks?,
    val publishedDate: String?,
    val pageCount: Int?
)

data class ImageLinks(
    val thumbnail: String?
)
```

**2. Agregar en RemoteModule:**

```kotlin
@Provides
@Singleton
fun provideGoogleBooksApi(): GoogleBooksApi {
    return Retrofit.Builder()
        .baseUrl("https://www.googleapis.com/books/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()
        .create(GoogleBooksApi::class.java)
}
```

**3. Usar en ViewModel:**

```kotlin
class BookDetailViewModel(
    private val bookApi: BookApi,
    private val googleBooksApi: GoogleBooksApi
) : ViewModel() {
    
    fun loadBookDetails(bookId: Long, isbn: String?) {
        viewModelScope.launch {
            // Datos del microservicio
            val localBook = bookApi.getBook(bookId)
            
            // Datos de API externa (si hay ISBN)
            if (isbn != null) {
                val externalData = googleBooksApi.searchBooks("isbn:$isbn")
                // Combinar datos
            }
        }
    }
}
```

## 📚 Opción 2: Open Library API

### Configuración

**1. Agregar API:**

```kotlin
interface OpenLibraryApi {
    @GET("api/books")
    suspend fun getBookByIsbn(
        @Query("bibkeys") isbn: String,
        @Query("format") format: String = "json"
    ): Response<Map<String, OpenLibraryBook>>
}

data class OpenLibraryBook(
    val title: String,
    val authors: List<Author>?,
    val cover: Cover?
)

data class Author(
    val name: String
)

data class Cover(
    val large: String?,
    val medium: String?,
    val small: String?
)
```

**2. Agregar en RemoteModule:**

```kotlin
@Provides
@Singleton
fun provideOpenLibraryApi(): OpenLibraryApi {
    return Retrofit.Builder()
        .baseUrl("https://openlibrary.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()
        .create(OpenLibraryApi::class.java)
}
```

## 🎯 Ejemplo de Uso en UI

```kotlin
@Composable
fun BookDetailScreen(
    bookId: Long,
    viewModel: BookDetailViewModel = hiltViewModel()
) {
    val book by viewModel.book.collectAsState()
    val externalData by viewModel.externalData.collectAsState()
    
    Column {
        // Datos del microservicio
        Text(book?.title ?: "")
        Text(book?.author ?: "")
        
        // Datos de API externa (sin interferir)
        if (externalData != null) {
            Divider()
            Text("Información adicional:", style = MaterialTheme.typography.titleMedium)
            Text(externalData.description ?: "")
            externalData.cover?.large?.let { url ->
                AsyncImage(model = url, contentDescription = null)
            }
        }
    }
}
```

## ✅ Checklist

- [ ] API externa configurada en RemoteModule
- [ ] DTOs creados para API externa
- [ ] Integración en ViewModel
- [ ] Mostrado en UI sin interferir con datos locales
- [ ] Manejo de errores implementado
- [ ] Documentado en README

