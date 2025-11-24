# 📚 Sincronización de Libros - Android ↔ Microservicio

## 🎯 Objetivo

Permitir la sincronización bidireccional de libros entre la aplicación Android y el microservicio de catálogo de libros, permitiendo:

1. **Cargar libros existentes** desde Android al servidor
2. **Agregar nuevos libros** desde el panel de admin
3. **Eliminar libros** y que se sincronice en ambos lados
4. **Actualizar libros** existentes

---

## 🔄 Flujo de Sincronización

### 1. Sincronización Inicial (Android → Servidor)

Cuando la app Android se inicia o el usuario solicita sincronizar:

```kotlin
// En Android
viewModel.loadAllBooksFromServer() // Obtiene todos los libros del servidor
viewModel.syncBooksToServer(localBooks) // Envía libros locales al servidor
```

**Endpoint:** `GET /api/books/all`
- Retorna todos los libros sin paginación
- Útil para comparar con libros locales

**Endpoint:** `POST /api/books/sync`
- Recibe lista de libros desde Android
- Crea nuevos o actualiza existentes basándose en:
  - ISBN (si está disponible)
  - Título + Autor (si no hay ISBN)

---

### 2. Agregar Libros desde Admin

El admin puede agregar libros de dos formas:

**Opción A: Individual**
```http
POST /api/books
Content-Type: application/json

{
  "title": "Nuevo Libro",
  "author": "Autor",
  ...
}
```

**Opción B: Masivo (Bulk)**
```http
POST /api/books/bulk
Content-Type: application/json

[
  { "title": "Libro 1", ... },
  { "title": "Libro 2", ... }
]
```

---

### 3. Eliminar Libros

**Individual:**
```http
DELETE /api/books/{bookId}
```

**Masivo:**
```http
DELETE /api/books/bulk
Content-Type: application/json

[1, 2, 3, 4, 5]
```

Android puede llamar a este endpoint cuando se eliminan libros localmente.

---

## 📡 Endpoints Disponibles

### Obtener Todos los Libros (Sin Paginación)

**GET** `/api/books/all`

**Respuesta:**
```json
[
  {
    "id": 1,
    "title": "El Quijote",
    "author": "Miguel de Cervantes",
    ...
  },
  ...
]
```

**Uso en Android:**
```kotlin
val response = bookApiService.getAllBooksWithoutPagination()
val allBooks = response.body() // Lista completa
```

---

### Sincronizar Libros

**POST** `/api/books/sync`

**Request:**
```json
[
  {
    "id": null,
    "title": "Libro desde Android",
    "author": "Autor",
    "isbn": "1234567890",
    "totalCopies": 5,
    "availableCopies": 3
  },
  {
    "id": 10,
    "title": "Libro Actualizado",
    "author": "Autor",
    "isbn": "0987654321"
  }
]
```

**Respuesta:**
```json
{
  "totalProcessed": 2,
  "created": 1,
  "updated": 1,
  "skipped": 0,
  "errors": 0,
  "createdBooks": [...],
  "updatedBooks": [...],
  "errorMessages": []
}
```

**Lógica de Sincronización:**
1. Busca por ISBN (si está disponible)
2. Si no encuentra, busca por título + autor (case-insensitive)
3. Si encuentra: **actualiza** el libro existente
4. Si no encuentra: **crea** un nuevo libro

---

### Crear Múltiples Libros (Bulk)

**POST** `/api/books/bulk`

**Request:**
```json
[
  {
    "title": "Libro 1",
    "author": "Autor 1",
    "totalCopies": 1
  },
  {
    "title": "Libro 2",
    "author": "Autor 2",
    "totalCopies": 1
  }
]
```

**Respuesta:**
```json
[
  { "id": 1, "title": "Libro 1", ... },
  { "id": 2, "title": "Libro 2", ... }
]
```

---

### Eliminar Múltiples Libros

**DELETE** `/api/books/bulk`

**Request:**
```json
[1, 2, 3, 4, 5]
```

**Respuesta:** `204 No Content`

---

## 🔧 Implementación en Android

### 1. Obtener Libros del Servidor

```kotlin
@HiltViewModel
class BookSyncViewModel @Inject constructor(
    private val bookSyncRepository: BookSyncRepository
) : ViewModel() {

    fun loadAllBooksFromServer() {
        viewModelScope.launch {
            val response = bookSyncRepository.getAllBooksFromServer()
            if (response.isSuccessful) {
                val serverBooks = response.body() ?: emptyList()
                // Comparar con libros locales y sincronizar
            }
        }
    }
}
```

### 2. Sincronizar Libros Locales al Servidor

```kotlin
fun syncLocalBooksToServer() {
    viewModelScope.launch {
        // Obtener libros de la base de datos local
        val localBooks = localBookDao.getAllBooks()
        
        // Convertir a BookSyncDTO
        val syncDTOs = localBooks.map { book ->
            BookSyncDTO(
                id = book.id,
                title = book.title,
                author = book.author,
                isbn = book.isbn,
                // ... otros campos
            )
        }
        
        // Sincronizar
        val response = bookSyncRepository.syncBooksToServer(syncDTOs)
        if (response.isSuccessful) {
            val result = response.body()
            // Mostrar resultado: X creados, Y actualizados
        }
    }
}
```

### 3. Eliminar Libros del Servidor

```kotlin
fun deleteBooksFromServer(bookIds: List<Long>) {
    viewModelScope.launch {
        val response = bookSyncRepository.deleteBooksBulk(bookIds)
        if (response.isSuccessful) {
            // Libros eliminados exitosamente
        }
    }
}
```

---

## 📋 Casos de Uso

### Caso 1: Usuario agrega libro en Android

1. Usuario crea libro en Android (se guarda localmente)
2. Al sincronizar, se envía al servidor con `POST /api/books/sync`
3. Servidor crea el libro y retorna el ID
4. Android actualiza el ID local del libro

### Caso 2: Admin agrega libro desde panel

1. Admin crea libro en el panel (servidor)
2. Android obtiene todos los libros con `GET /api/books/all`
3. Compara con libros locales
4. Si no existe localmente, lo agrega a la base de datos local

### Caso 3: Usuario elimina libro en Android

1. Usuario elimina libro en Android
2. Al sincronizar, se envía lista de IDs eliminados con `DELETE /api/books/bulk`
3. Servidor elimina los libros
4. Android confirma eliminación

### Caso 4: Admin elimina libro desde panel

1. Admin elimina libro en el servidor
2. Android obtiene todos los libros con `GET /api/books/all`
3. Compara con libros locales
4. Si un libro local no existe en el servidor, lo elimina localmente

---

## ⚠️ Consideraciones

### Identificación de Libros

El sistema identifica libros duplicados por:
1. **ISBN** (prioridad alta) - Si dos libros tienen el mismo ISBN, se consideran el mismo
2. **Título + Autor** (prioridad baja) - Si no hay ISBN, se usa título y autor

### Validaciones

- Título y autor son **obligatorios**
- ISBN debe ser **único** (si se proporciona)
- Copias totales debe ser **≥ 1**
- Si `availableCopies` no se proporciona en sync, se calcula automáticamente

### Manejo de Errores

La sincronización masiva continúa aunque algunos libros fallen:
- Libros válidos se procesan
- Libros con errores se reportan en `errorMessages`
- Se retorna resumen: creados, actualizados, omitidos, errores

---

## 🔄 Estrategia de Sincronización Recomendada

### Sincronización Bidireccional

1. **Android → Servidor:**
   - Enviar libros locales que no tienen ID del servidor
   - Enviar libros modificados localmente

2. **Servidor → Android:**
   - Obtener todos los libros del servidor
   - Comparar con locales
   - Agregar nuevos, actualizar existentes, eliminar removidos

### Timestamps (Opcional)

Para mejor sincronización, considerar agregar:
- `lastModified` en cada libro
- Sincronizar solo libros modificados después de última sincronización

---

## 📝 Ejemplo Completo de Uso

```kotlin
// 1. Obtener todos los libros del servidor
val serverBooks = bookSyncRepository.getAllBooksFromServer().body() ?: emptyList()

// 2. Obtener libros locales
val localBooks = localBookDao.getAllBooks()

// 3. Identificar diferencias
val serverBookIds = serverBooks.map { it.id }.toSet()
val localBookIds = localBooks.map { it.serverId }.filterNotNull().toSet()

// Libros nuevos en servidor (agregar localmente)
val newBooks = serverBooks.filter { it.id !in localBookIds }

// Libros eliminados en servidor (eliminar localmente)
val deletedBooks = localBooks.filter { 
    it.serverId != null && it.serverId !in serverBookIds 
}

// Libros nuevos localmente (enviar al servidor)
val localNewBooks = localBooks.filter { it.serverId == null }

// 4. Sincronizar
if (localNewBooks.isNotEmpty()) {
    val syncDTOs = localNewBooks.map { convertToSyncDTO(it) }
    bookSyncRepository.syncBooksToServer(syncDTOs)
}

// 5. Actualizar localmente
newBooks.forEach { addToLocal(it) }
deletedBooks.forEach { deleteFromLocal(it.id) }
```

---

**Última actualización:** 2024-01-15

