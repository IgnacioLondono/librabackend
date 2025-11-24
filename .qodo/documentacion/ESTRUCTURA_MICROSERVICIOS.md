# 📚 Estructura Completa de Microservicios - Biblioteca Digital

## 🏗️ Arquitectura General

El sistema está compuesto por **5 microservicios independientes**, cada uno con su propia base de datos MySQL:

1. **User Management Service** (Puerto 8081) - Gestión de usuarios y autenticación
2. **Book Catalog Service** (Puerto 8082) - Catálogo de libros
3. **Loan Management Service** (Puerto 8083) - Gestión de préstamos
4. **Reports Service** (Puerto 8084) - Reportes y estadísticas
5. **Notifications Service** (Puerto 8085) - Sistema de notificaciones

### 🔐 Autenticación

Todos los servicios (excepto login/register) requieren autenticación JWT:
```
Authorization: Bearer {token}
```

El token se obtiene del endpoint `/api/users/login` y tiene una validez de 24 horas.

---

## 1️⃣ User Management Service (Puerto 8081)

### 📋 Descripción
Gestiona usuarios, autenticación, sesiones y roles. Es el servicio central de autenticación.

### 🔗 Base URL
```
http://localhost:8081/api/users
```

### 📡 Endpoints

#### 🔓 Públicos (Sin autenticación)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `POST` | `/register` | Registrar nuevo usuario |
| `POST` | `/login` | Iniciar sesión y obtener token JWT |

#### 🔒 Protegidos (Requieren JWT)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `POST` | `/logout` | Cerrar sesión |
| `GET` | `/{userId}` | Obtener usuario por ID |
| `GET` | `/` | Listar todos los usuarios |
| `PUT` | `/{userId}` | Actualizar usuario |
| `PATCH` | `/{userId}/block` | Bloquear/desbloquear usuario |
| `PATCH` | `/{userId}/role` | Cambiar rol de usuario |
| `POST` | `/validate-token` | Validar token JWT |
| `DELETE` | `/{userId}` | Eliminar usuario |

### 📦 DTOs (Data Transfer Objects)

#### UserRegistrationDTO (Request)
```json
{
  "name": "Juan Pérez",
  "email": "juan@example.com",
  "phone": "123456789",
  "password": "password123"
}
```

#### UserLoginDTO (Request)
```json
{
  "email": "juan@example.com",
  "password": "password123"
}
```

#### LoginResponseDTO (Response)
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": 1,
    "name": "Juan Pérez",
    "email": "juan@example.com",
    "phone": "123456789",
    "role": "USUARIO",
    "status": "ACTIVO",
    "profileImageUri": null,
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-15T10:30:00"
  },
  "expiresIn": 86400
}
```

#### UserResponseDTO (Response)
```json
{
  "id": 1,
  "name": "Juan Pérez",
  "email": "juan@example.com",
  "phone": "123456789",
  "role": "USUARIO",
  "status": "ACTIVO",
  "profileImageUri": "https://example.com/image.jpg",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

#### UserUpdateDTO (Request)
```json
{
  "name": "Juan Carlos Pérez",
  "phone": "987654321",
  "profileImageUri": "https://example.com/new-image.jpg"
}
```

#### TokenValidationDTO (Request/Response)
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "valid": true,
  "userId": 1,
  "message": "Token válido"
}
```

### 🎭 Roles
- `USUARIO` - Usuario regular
- `ADMINISTRADOR` - Administrador del sistema

### 📊 Estados
- `ACTIVO` - Usuario activo
- `BLOQUEADO` - Usuario bloqueado

---

## 2️⃣ Book Catalog Service (Puerto 8082)

### 📋 Descripción
Gestiona el catálogo de libros: crear, actualizar, buscar, eliminar libros y verificar disponibilidad.

### 🔗 Base URL
```
http://localhost:8082/api/books
```

### 📡 Endpoints

| Método | Endpoint | Descripción | Autenticación |
|--------|----------|-------------|---------------|
| `POST` | `/` | Crear nuevo libro | ✅ Admin |
| `GET` | `/{bookId}` | Obtener libro por ID | ❌ |
| `GET` | `/` | Listar libros (paginado) | ❌ |
| `GET` | `/search?q={query}` | Buscar libros | ❌ |
| `GET` | `/category/{category}` | Libros por categoría | ❌ |
| `GET` | `/featured` | Libros destacados | ❌ |
| `PUT` | `/{bookId}` | Actualizar libro | ✅ Admin |
| `DELETE` | `/{bookId}` | Eliminar libro | ✅ Admin |
| `GET` | `/{bookId}/availability` | Verificar disponibilidad | ❌ |
| `PATCH` | `/{bookId}/copies?change={number}` | Actualizar copias | ✅ Admin |

### 📦 DTOs

#### BookCreateDTO (Request)
```json
{
  "title": "El Quijote",
  "author": "Miguel de Cervantes",
  "isbn": "978-84-376-0494-7",
  "category": "Literatura Clásica",
  "publisher": "Editorial XYZ",
  "year": 1605,
  "description": "La obra maestra de la literatura española",
  "coverUrl": "https://example.com/quijote.jpg",
  "totalCopies": 10,
  "price": 29.99,
  "featured": true
}
```

#### BookResponseDTO (Response)
```json
{
  "id": 1,
  "title": "El Quijote",
  "author": "Miguel de Cervantes",
  "isbn": "978-84-376-0494-7",
  "category": "Literatura Clásica",
  "publisher": "Editorial XYZ",
  "year": 1605,
  "description": "La obra maestra de la literatura española",
  "coverUrl": "https://example.com/quijote.jpg",
  "status": "AVAILABLE",
  "totalCopies": 10,
  "availableCopies": 8,
  "price": 29.99,
  "featured": true,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

#### BookUpdateDTO (Request)
```json
{
  "title": "Don Quijote de la Mancha",
  "author": "Miguel de Cervantes Saavedra",
  "category": "Literatura",
  "totalCopies": 15,
  "price": 34.99,
  "featured": false
}
```

#### BookAvailabilityDTO (Response)
```json
{
  "bookId": 1,
  "available": true,
  "availableCopies": 8,
  "totalCopies": 10,
  "message": "El libro está disponible"
}
```

### 📄 Paginación
Los endpoints de listado usan paginación:
```
GET /api/books?page=0&size=10&sortBy=title&sortDir=ASC
```

---

## 3️⃣ Loan Management Service (Puerto 8083)

### 📋 Descripción
Gestiona préstamos de libros: crear, devolver, extender, cancelar préstamos y calcular multas.

### 🔗 Base URL
```
http://localhost:8083/api/loans
```

### 📡 Endpoints

| Método | Endpoint | Descripción | Autenticación |
|--------|----------|-------------|---------------|
| `POST` | `/` | Crear nuevo préstamo | ✅ |
| `GET` | `/{loanId}` | Obtener préstamo por ID | ✅ |
| `GET` | `/user/{userId}` | Préstamos de un usuario | ✅ |
| `GET` | `/user/{userId}/status?status={status}` | Préstamos por estado | ✅ |
| `GET` | `/user/{userId}/active` | Préstamos activos | ✅ |
| `GET` | `/book/{bookId}` | Préstamos de un libro | ✅ |
| `POST` | `/{loanId}/return` | Registrar devolución | ✅ |
| `PATCH` | `/{loanId}/extend` | Extender préstamo | ✅ |
| `PATCH` | `/{loanId}/cancel` | Cancelar préstamo | ✅ |
| `GET` | `/overdue` | Préstamos vencidos | ✅ Admin |
| `GET` | `/{loanId}/fine` | Calcular multa | ✅ |
| `GET` | `/{loanId}/history` | Historial del préstamo | ✅ |
| `POST` | `/validate` | Validar creación de préstamo | ✅ |

### 📦 DTOs

#### LoanCreateDTO (Request)
```json
{
  "userId": 1,
  "bookId": 5,
  "loanDays": 14
}
```

#### LoanResponseDTO (Response)
```json
{
  "id": 1,
  "userId": 1,
  "bookId": 5,
  "loanDate": "2024-01-15",
  "dueDate": "2024-01-29",
  "returnDate": null,
  "status": "ACTIVE",
  "loanDays": 14,
  "fineAmount": 0.00,
  "extensionsCount": 0,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

#### LoanValidationDTO (Response)
```json
{
  "userId": 1,
  "bookId": 5,
  "userExists": true,
  "bookAvailable": true,
  "valid": true,
  "message": "Validación exitosa"
}
```

#### LoanHistoryResponseDTO (Response)
```json
{
  "id": 1,
  "loanId": 1,
  "action": "CREATED",
  "notes": "Préstamo creado",
  "timestamp": "2024-01-15T10:30:00"
}
```

### 📊 Estados de Préstamo
- `ACTIVE` - Préstamo activo
- `RETURNED` - Libro devuelto
- `OVERDUE` - Préstamo vencido
- `CANCELLED` - Préstamo cancelado

### ⚠️ Reglas de Negocio
- Máximo 5 préstamos activos por usuario
- Préstamo por defecto: 14 días
- Máximo 2 extensiones por préstamo
- Multa por día vencido: configurable

---

## 4️⃣ Reports Service (Puerto 8084)

### 📋 Descripción
Genera reportes y estadísticas del sistema. Consume datos de otros microservicios.

### 🔗 Base URL
```
http://localhost:8084/api/reports
```

### 📡 Endpoints

| Método | Endpoint | Descripción | Autenticación |
|--------|----------|-------------|---------------|
| `GET` | `/dashboard` | Estadísticas del dashboard | ✅ Admin |

### 📦 DTOs

#### DashboardStatisticsDTO (Response)
```json
{
  "totalBooks": 150,
  "totalUsers": 50,
  "totalLoans": 200,
  "activeLoans": 30,
  "overdueLoans": 5,
  "availableBooks": 120,
  "loanedBooks": 30,
  "revenue": 0.00,
  "dateRange": "Últimos 30 días"
}
```

---

## 5️⃣ Notifications Service (Puerto 8085)

### 📋 Descripción
Gestiona notificaciones para usuarios: préstamos, vencimientos, disponibilidad de libros, etc.

### 🔗 Base URL
```
http://localhost:8085/api/notifications
```

### 📡 Endpoints

| Método | Endpoint | Descripción | Autenticación |
|--------|----------|-------------|---------------|
| `POST` | `/` | Crear notificación | ✅ |
| `GET` | `/user/{userId}` | Notificaciones de usuario | ✅ |
| `GET` | `/user/{userId}?unreadOnly=true` | Solo no leídas | ✅ |
| `PATCH` | `/{notificationId}/read` | Marcar como leída | ✅ |
| `PATCH` | `/user/{userId}/read-all` | Marcar todas como leídas | ✅ |
| `DELETE` | `/{notificationId}` | Eliminar notificación | ✅ |
| `GET` | `/user/{userId}/unread-count` | Contador de no leídas | ✅ |

### 📦 DTOs

#### NotificationCreateDTO (Request)
```json
{
  "userId": 1,
  "type": "LOAN_DUE",
  "title": "Préstamo próximo a vencer",
  "message": "Tu préstamo del libro 'El Quijote' vence mañana",
  "priority": "HIGH"
}
```

#### NotificationResponseDTO (Response)
```json
{
  "id": 1,
  "userId": 1,
  "type": "LOAN_DUE",
  "title": "Préstamo próximo a vencer",
  "message": "Tu préstamo del libro 'El Quijote' vence mañana",
  "read": false,
  "priority": "HIGH",
  "createdAt": "2024-01-15T10:30:00"
}
```

### 📢 Tipos de Notificación
- `LOAN_CREATED` - Préstamo creado
- `LOAN_DUE` - Préstamo próximo a vencer
- `LOAN_OVERDUE` - Préstamo vencido
- `BOOK_AVAILABLE` - Libro disponible
- `SYSTEM` - Notificación del sistema

### 🎯 Prioridades
- `LOW` - Baja
- `MEDIUM` - Media
- `HIGH` - Alta

---

## 🔄 Comunicación Entre Servicios

### Flujo de Creación de Préstamo

1. **Cliente Android** → `POST /api/loans` (Loan Service)
2. **Loan Service** → `GET /api/users/{userId}/validate` (User Service) - Validar usuario
3. **Loan Service** → `GET /api/books/{bookId}/availability` (Book Service) - Verificar disponibilidad
4. **Loan Service** → `PATCH /api/books/{bookId}/copies?change=-1` (Book Service) - Reducir copias
5. **Loan Service** → `POST /api/notifications` (Notification Service) - Notificar al usuario

### Flujo de Devolución

1. **Cliente Android** → `POST /api/loans/{loanId}/return` (Loan Service)
2. **Loan Service** → `PATCH /api/books/{bookId}/copies?change=1` (Book Service) - Aumentar copias
3. **Loan Service** → `POST /api/notifications` (Notification Service) - Confirmar devolución

---

## 🔐 Manejo de Autenticación en Android

### 1. Login
```kotlin
POST http://localhost:8081/api/users/login
Body: {
  "email": "usuario@example.com",
  "password": "password123"
}
Response: {
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {...},
  "expiresIn": 86400
}
```

### 2. Guardar Token
Guardar el token en SharedPreferences o SecureStorage para usarlo en todas las peticiones.

### 3. Usar Token en Peticiones
```kotlin
Headers: {
  "Authorization": "Bearer {token}"
}
```

### 4. Interceptor de Retrofit
```kotlin
class AuthInterceptor(private val tokenManager: TokenManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer ${tokenManager.getToken()}")
            .build()
        return chain.proceed(request)
    }
}
```

---

## 📱 Ejemplos de Uso para Android

### 1. Registrar Usuario
```kotlin
POST /api/users/register
{
  "name": "Juan Pérez",
  "email": "juan@example.com",
  "phone": "123456789",
  "password": "password123"
}
```

### 2. Iniciar Sesión
```kotlin
POST /api/users/login
{
  "email": "juan@example.com",
  "password": "password123"
}
// Guardar token recibido
```

### 3. Buscar Libros
```kotlin
GET /api/books/search?q=quijote&page=0&size=10
```

### 4. Crear Préstamo
```kotlin
POST /api/loans
Headers: Authorization: Bearer {token}
{
  "userId": 1,
  "bookId": 5,
  "loanDays": 14
}
```

### 5. Obtener Préstamos del Usuario
```kotlin
GET /api/loans/user/1
Headers: Authorization: Bearer {token}
```

### 6. Obtener Notificaciones
```kotlin
GET /api/notifications/user/1?unreadOnly=true
Headers: Authorization: Bearer {token}
```

---

## ⚠️ Manejo de Errores

Todos los servicios devuelven errores en el siguiente formato:

```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "El email ya está registrado",
  "path": "/api/users/register",
  "details": []
}
```

### Códigos HTTP Comunes
- `200 OK` - Operación exitosa
- `201 Created` - Recurso creado
- `400 Bad Request` - Solicitud inválida
- `401 Unauthorized` - Token inválido o faltante
- `403 Forbidden` - Sin permisos
- `404 Not Found` - Recurso no encontrado
- `500 Internal Server Error` - Error del servidor

---

## 🔧 URLs de Desarrollo

Para desarrollo local con emulador Android:
- User Service: `http://10.0.2.2:8081`
- Book Service: `http://10.0.2.2:8082`
- Loan Service: `http://10.0.2.2:8083`
- Reports Service: `http://10.0.2.2:8084`
- Notifications Service: `http://10.0.2.2:8085`

Para dispositivo físico:
- Reemplazar `10.0.2.2` con la IP local de tu máquina (ej: `192.168.1.100`)

---

## 📚 Swagger/OpenAPI

Cada servicio tiene documentación Swagger disponible en:
- User Service: `http://localhost:8081/swagger-ui.html`
- Book Service: `http://localhost:8082/swagger-ui.html`
- Loan Service: `http://localhost:8083/swagger-ui.html`
- Reports Service: `http://localhost:8084/swagger-ui.html`
- Notifications Service: `http://localhost:8085/swagger-ui.html`

---

## ✅ Checklist para Android

- [ ] Implementar interceptor de autenticación JWT
- [ ] Manejar almacenamiento seguro del token
- [ ] Implementar refresh token (si se agrega)
- [ ] Manejar errores 401 (token expirado) y redirigir a login
- [ ] Implementar paginación en listados
- [ ] Manejar estados de carga y errores
- [ ] Validar formularios antes de enviar
- [ ] Implementar pull-to-refresh
- [ ] Cachear datos cuando sea apropiado
- [ ] Manejar notificaciones en tiempo real (WebSocket opcional)

---

## 📞 Contacto y Soporte

Para dudas sobre la integración, consultar:
- Documentación Swagger de cada servicio
- Logs de los microservicios
- Este documento

---

**Última actualización:** 2024-01-15

