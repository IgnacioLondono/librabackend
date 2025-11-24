# 🧪 Cómo Hacer Pruebas Unitarias en Android Studio

## 📋 Configuración Inicial

### 1. Dependencias en build.gradle.kts

```kotlin
dependencies {
    // Testing
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.mockito:mockito-core:5.7.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.2.1")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    testImplementation("androidx.arch.core:core-testing:2.2.0")
    testImplementation("com.squareup.okhttp3:mockwebserver:4.12.0")
    
    // Para ViewModels
    testImplementation("androidx.lifecycle:lifecycle-viewmodel-testing:2.7.0")
}
```

### 2. Estructura de Carpetas

```
app/src/
├── main/
│   └── java/com/example/uinavegacion/
│       ├── data/
│       ├── domain/
│       └── presentation/
└── test/  ← Tests unitarios aquí
    └── java/com/example/uinavegacion/
        ├── data/
        ├── domain/
        └── presentation/
```

## 🧪 Ejemplos de Pruebas Unitarias

### Test de ViewModel

**Archivo:** `test/presentation/viewmodel/LoginViewModelTest.kt`

```kotlin
package com.example.uinavegacion.presentation.viewmodel

import com.example.uinavegacion.data.remote.dto.LoginRequestDto
import com.example.uinavegacion.data.remote.dto.LoginResponseDto
import com.example.uinavegacion.data.remote.dto.UserApi
import com.example.uinavegacion.data.remote.dto.UserDto
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.*
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    @Mock
    private lateinit var userApi: UserApi

    private lateinit var viewModel: LoginViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        viewModel = LoginViewModel(userApi)
    }

    @Test
    fun `login exitoso actualiza estado correctamente`() = runTest {
        // Arrange (Preparar)
        val email = "test@example.com"
        val password = "password123"
        val loginRequest = LoginRequestDto(email, password)
        
        val userDto = UserDto(
            id = 1,
            name = "Test User",
            email = email,
            phone = null,
            role = "USUARIO",
            status = "ACTIVO",
            profileImageUri = null,
            createdAt = "2024-01-01T00:00:00",
            updatedAt = null
        )
        
        val loginResponse = LoginResponseDto(
            token = "test-token-123",
            user = userDto,
            expiresIn = 86400000
        )

        whenever(userApi.login(loginRequest))
            .thenReturn(Response.success(loginResponse))

        // Act (Actuar)
        viewModel.login(email, password)

        // Assert (Verificar)
        assertEquals("test-token-123", viewModel.token.value)
        assertEquals(userDto, viewModel.user.value)
        assertFalse(viewModel.isLoading.value)
        assertNull(viewModel.error.value)
        
        // Verificar que se llamó la API
        verify(userApi, times(1)).login(loginRequest)
    }

    @Test
    fun `login fallido muestra error`() = runTest {
        // Arrange
        val email = "test@example.com"
        val password = "wrong"
        val loginRequest = LoginRequestDto(email, password)

        whenever(userApi.login(loginRequest))
            .thenReturn(Response.error(401, okhttp3.ResponseBody.create(null, "")))

        // Act
        viewModel.login(email, password)

        // Assert
        assertNotNull(viewModel.error.value)
        assertTrue(viewModel.error.value!!.contains("Error"))
    }
}
```

### Test de Repository

**Archivo:** `test/data/repository/BookRepositoryTest.kt`

```kotlin
package com.example.uinavegacion.data.repository

import com.example.uinavegacion.data.remote.dto.BookApi
import com.example.uinavegacion.data.remote.dto.BookDto
import com.example.uinavegacion.data.remote.dto.BookPageResponseDto
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.*
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class BookRepositoryTest {

    @Mock
    private lateinit var bookApi: BookApi

    private lateinit var repository: BookRepository

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        repository = BookRepository(bookApi)
    }

    @Test
    fun `obtener libros retorna lista correcta`() = runTest {
        // Arrange
        val books = listOf(
            BookDto(
                id = 1,
                title = "El Quijote",
                author = "Miguel de Cervantes",
                isbn = "978-84-376-0494-7",
                category = "Literatura",
                publisher = "Editorial XYZ",
                year = 1605,
                description = "Obra maestra",
                coverUrl = null,
                status = "AVAILABLE",
                totalCopies = 5,
                availableCopies = 3,
                price = 25.99,
                featured = true,
                createdAt = "2024-01-01T00:00:00",
                updatedAt = null
            )
        )
        
        val pageResponse = BookPageResponseDto(
            content = books,
            totalElements = 1,
            totalPages = 1,
            size = 10,
            number = 0
        )

        whenever(bookApi.getAllBooks(0, 10, "title", "ASC"))
            .thenReturn(Response.success(pageResponse))

        // Act
        val result = repository.getBooks(0, 10)

        // Assert
        assertEquals(1, result.size)
        assertEquals("El Quijote", result[0].title)
        assertEquals("Miguel de Cervantes", result[0].author)
        verify(bookApi, times(1)).getAllBooks(0, 10, "title", "ASC")
    }

    @Test
    fun `buscar libros retorna resultados filtrados`() = runTest {
        // Arrange
        val query = "quijote"
        val books = listOf(
            BookDto(1, "El Quijote", "Cervantes", null, null, null, null, null, null, "AVAILABLE", 5, 3, null, false, "", null)
        )
        val pageResponse = BookPageResponseDto(books, 1, 1, 10, 0)

        whenever(bookApi.searchBooks(query, 0, 10))
            .thenReturn(Response.success(pageResponse))

        // Act
        val result = repository.searchBooks(query)

        // Assert
        assertEquals(1, result.size)
        assertTrue(result[0].title.contains(query, ignoreCase = true))
    }
}
```

### Test de Use Case

**Archivo:** `test/domain/usecase/CreateLoanUseCaseTest.kt`

```kotlin
package com.example.uinavegacion.domain.usecase

import com.example.uinavegacion.data.remote.dto.BookApi
import com.example.uinavegacion.data.remote.dto.BookAvailabilityDto
import com.example.uinavegacion.data.remote.dto.LoanApi
import com.example.uinavegacion.data.remote.dto.LoanDto
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.*
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class CreateLoanUseCaseTest {

    @Mock
    private lateinit var loanApi: LoanApi

    @Mock
    private lateinit var bookApi: BookApi

    private lateinit var useCase: CreateLoanUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        useCase = CreateLoanUseCase(loanApi, bookApi)
    }

    @Test
    fun `crear prestamo exitoso cuando libro esta disponible`() = runTest {
        // Arrange
        val userId = 1L
        val bookId = 1L
        val token = "Bearer test-token"

        val availability = BookAvailabilityDto(
            bookId = bookId,
            available = true,
            availableCopies = 3,
            totalCopies = 5,
            message = "Disponible"
        )

        val loanDto = LoanDto(
            id = 1,
            userId = userId,
            bookId = bookId,
            loanDate = "2024-01-01",
            dueDate = "2024-01-15",
            returnDate = null,
            status = "ACTIVE",
            loanDays = 14,
            fineAmount = 0.0,
            extensionsCount = 0,
            createdAt = "2024-01-01T00:00:00",
            updatedAt = null
        )

        whenever(bookApi.checkAvailability(bookId))
            .thenReturn(Response.success(availability))

        whenever(loanApi.createLoan(any(), eq(token)))
            .thenReturn(Response.success(loanDto))

        // Act
        val result = useCase(userId, bookId, token)

        // Assert
        assertNotNull(result)
        assertEquals(1L, result?.id)
        assertEquals("ACTIVE", result?.status)
        
        // Verificar que se verificó disponibilidad primero
        verify(bookApi, times(1)).checkAvailability(bookId)
        verify(loanApi, times(1)).createLoan(any(), eq(token))
    }

    @Test
    fun `crear prestamo falla cuando libro no esta disponible`() = runTest {
        // Arrange
        val userId = 1L
        val bookId = 1L
        val token = "Bearer test-token"

        val availability = BookAvailabilityDto(
            bookId = bookId,
            available = false,
            availableCopies = 0,
            totalCopies = 5,
            message = "No disponible"
        )

        whenever(bookApi.checkAvailability(bookId))
            .thenReturn(Response.success(availability))

        // Act
        val result = useCase(userId, bookId, token)

        // Assert
        assertNull(result)
        verify(bookApi, times(1)).checkAvailability(bookId)
        verify(loanApi, never()).createLoan(any(), any())
    }
}
```

### Test de API con MockWebServer

**Archivo:** `test/data/remote/dto/UserApiTest.kt`

```kotlin
package com.example.uinavegacion.data.remote.dto

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.Gson

@OptIn(ExperimentalCoroutinesApi::class)
class UserApiTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var userApi: UserApi
    private val gson = Gson()

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        userApi = retrofit.create(UserApi::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `login retorna respuesta correcta`() = runTest {
        // Arrange
        val loginRequest = LoginRequestDto("test@example.com", "password123")
        val loginResponse = LoginResponseDto(
            token = "test-token",
            user = UserDto(1, "Test", "test@example.com", null, "USUARIO", "ACTIVO", null, "", null),
            expiresIn = 86400000
        )

        val jsonResponse = gson.toJson(loginResponse)
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(jsonResponse)
        )

        // Act
        val response = userApi.login(loginRequest)

        // Assert
        assertTrue(response.isSuccessful)
        assertEquals("test-token", response.body()?.token)
        assertEquals(1, response.body()?.user?.id)
    }

    @Test
    fun `login retorna error 401 cuando credenciales son invalidas`() = runTest {
        // Arrange
        val loginRequest = LoginRequestDto("wrong@example.com", "wrong")
        
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(401)
                .setBody("{\"message\":\"Credenciales inválidas\"}")
        )

        // Act
        val response = userApi.login(loginRequest)

        // Assert
        assertFalse(response.isSuccessful)
        assertEquals(401, response.code())
    }
}
```

## 🚀 Cómo Ejecutar las Pruebas

### En Android Studio

1. **Ejecutar todos los tests:**
   - Click derecho en carpeta `test/`
   - Seleccionar "Run 'Tests in 'test''"

2. **Ejecutar un test específico:**
   - Click en el icono ▶️ junto al método `@Test`
   - O click derecho en el método > "Run 'nombreTest'"

3. **Ejecutar desde terminal:**
   ```bash
   ./gradlew test
   ```

4. **Ver cobertura:**
   - Run > Run with Coverage
   - O: `./gradlew test jacocoTestReport`

### En Visual Studio Code

1. **Instalar extensión:**
   - "Extension Pack for Java"
   - "Kotlin Language"

2. **Ejecutar tests:**
   - Abrir archivo de test
   - Click en "Run Test" sobre el método `@Test`
   - O usar Command Palette: "Java: Run Tests"

3. **Ver resultados:**
   - Panel "Testing" en la barra lateral
   - Ver cobertura en "Java: Show Test Coverage"

## 📊 Verificar Cobertura

### Configurar JaCoCo

**build.gradle.kts:**
```kotlin
plugins {
    id("jacoco")
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    
    reports {
        xml.required = true
        html.required = true
    }
    
    executionData.setFrom(fileTree(layout.buildDirectory.dir("jacoco")).include("**/*.exec"))
    
    classDirectories.setFrom(
        files(classDirectories.files.map {
            fileTree(it) {
                exclude(
                    "**/R.class",
                    "**/R\$*.class",
                    "**/BuildConfig.*",
                    "**/Manifest*.*"
                )
            }
        })
    )
}
```

### Generar Reporte

```bash
./gradlew test jacocoTestReport
```

Reporte en: `app/build/reports/jacoco/test/html/index.html`

## ✅ Checklist de Pruebas

- [ ] Tests de ViewModels creados
- [ ] Tests de Repositories creados
- [ ] Tests de Use Cases creados
- [ ] Tests de APIs con MockWebServer
- [ ] Todos los tests pasan
- [ ] Cobertura > 80%
- [ ] Reporte de cobertura generado

