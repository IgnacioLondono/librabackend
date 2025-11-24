# Sistema de Biblioteca Digital - Arquitectura de Microservicios

Sistema completo de gestión de biblioteca digital implementado con arquitectura de microservicios usando Java Spring Boot.

## 🏗️ Arquitectura

El sistema está compuesto por 5 microservicios independientes:

1. **User Management Service** (Puerto 8081) - Gestión de usuarios y autenticación JWT
2. **Book Catalog Service** (Puerto 8082) - Catálogo e inventario de libros
3. **Loan Management Service** (Puerto 8083) - Gestión de préstamos
4. **Reports Service** (Puerto 8084) - Generación de reportes y estadísticas
5. **Notifications Service** (Puerto 8085) - Sistema de notificaciones

## 📋 Requisitos Previos

- **JDK 21** o superior
- **Maven 3.8+**
- **MySQL 8.0+** o **Docker** (para usar docker-compose)
- **Git**

## 🚀 Instalación y Configuración

### Opción 1: Usando Docker Compose (Recomendado)

1. **Iniciar las bases de datos MySQL:**

```bash
docker-compose up -d
```

Esto creará 5 instancias de MySQL, una para cada microservicio:
- `library_users_db` en puerto 3306
- `library_books_db` en puerto 3307
- `library_loans_db` en puerto 3308
- `library_reports_db` en puerto 3309
- `library_notifications_db` en puerto 3310

2. **Compilar y ejecutar cada microservicio:**

```bash
# User Management Service
cd user-management-service
mvn clean install
mvn spring-boot:run

# Book Catalog Service (en otra terminal)
cd book-catalog-service
mvn clean install
mvn spring-boot:run

# Loan Management Service (en otra terminal)
cd loan-management-service
mvn clean install
mvn spring-boot:run

# Reports Service (en otra terminal)
cd reports-service
mvn clean install
mvn spring-boot:run

# Notifications Service (en otra terminal)
cd notifications-service
mvn clean install
mvn spring-boot:run
```

### Opción 2: Configuración Manual de MySQL

1. **Crear las bases de datos:**

```sql
CREATE DATABASE library_users_db;
CREATE DATABASE library_books_db;
CREATE DATABASE library_loans_db;
CREATE DATABASE library_reports_db;
CREATE DATABASE library_notifications_db;
```

2. **Actualizar las configuraciones** en `application.yml` de cada servicio con tus credenciales de MySQL.

3. **Compilar y ejecutar** cada microservicio como se indica arriba.

## 📚 Documentación de APIs

Cada microservicio incluye documentación Swagger/OpenAPI disponible en:

- User Management: http://localhost:8081/swagger-ui.html
- Book Catalog: http://localhost:8082/swagger-ui.html
- Loan Management: http://localhost:8083/swagger-ui.html
- Reports: http://localhost:8084/swagger-ui.html
- Notifications: http://localhost:8085/swagger-ui.html

## 🔐 Autenticación JWT

El sistema utiliza autenticación JWT centralizada. Para usar los endpoints protegidos:

1. **Registrar un usuario:**
```bash
POST http://localhost:8081/api/users/register
{
  "name": "Juan Pérez",
  "email": "juan@example.com",
  "password": "password123",
  "phone": "123456789"
}
```

2. **Iniciar sesión:**
```bash
POST http://localhost:8081/api/users/login
{
  "email": "juan@example.com",
  "password": "password123"
}
```

3. **Usar el token** en las peticiones:
```
Authorization: Bearer <token>
```

## 🧪 Pruebas

Ejecutar pruebas unitarias para cada servicio:

```bash
cd <service-directory>
mvn test
```

## 📁 Estructura del Proyecto

```
libra_services/
├── user-management-service/
│   ├── src/main/java/com/library/users/
│   │   ├── controller/     # REST Controllers
│   │   ├── service/        # Lógica de negocio
│   │   ├── repository/     # Data Access
│   │   ├── model/          # Entidades JPA
│   │   ├── dto/            # Data Transfer Objects
│   │   ├── config/         # Configuraciones
│   │   ├── security/       # JWT y Security
│   │   └── exception/      # Manejo de excepciones
│   └── pom.xml
├── book-catalog-service/
├── loan-management-service/
├── reports-service/
├── notifications-service/
├── docker-compose.yml
└── README.md
```

## 🔧 Configuración

### Variables de Entorno

Para producción, configurar las siguientes variables de entorno o usar `application-prod.yml`:

- `DB_HOST`: Host de la base de datos
- `DB_PORT`: Puerto de la base de datos
- `DB_NAME`: Nombre de la base de datos
- `DB_USER`: Usuario de MySQL
- `DB_PASSWORD`: Contraseña de MySQL
- `JWT_SECRET`: Clave secreta para JWT (cambiar en producción)
- `JWT_EXPIRATION`: Tiempo de expiración del token en milisegundos

### Health Checks

Cada servicio expone endpoints de health check en:
- http://localhost:808X/actuator/health

## 📝 Endpoints Principales

### User Management Service

- `POST /api/users/register` - Registro de usuarios
- `POST /api/users/login` - Autenticación
- `GET /api/users/{userId}` - Obtener usuario
- `PUT /api/users/{userId}` - Actualizar usuario
- `POST /api/users/validate-token` - Validar token JWT

### Book Catalog Service

- `POST /api/books` - Crear libro
- `GET /api/books` - Listar libros (paginado)
- `GET /api/books/search?q={query}` - Buscar libros
- `GET /api/books/{bookId}/availability` - Verificar disponibilidad

### Loan Management Service

- `POST /api/loans` - Crear préstamo
- `GET /api/loans/user/{userId}/active` - Préstamos activos
- `POST /api/loans/{loanId}/return` - Registrar devolución
- `GET /api/loans/overdue` - Préstamos vencidos

### Reports Service

- `GET /api/reports/dashboard` - Estadísticas del dashboard

### Notifications Service

- `POST /api/notifications` - Crear notificación
- `GET /api/notifications/user/{userId}` - Notificaciones de usuario
- `GET /api/notifications/user/{userId}/unread-count` - Contador de no leídas

## 🛠️ Tecnologías Utilizadas

- **Java 21**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **Spring WebFlux** (WebClient)
- **MySQL 8.0**
- **JWT** (JSON Web Tokens)
- **Swagger/OpenAPI 3**
- **Lombok**
- **Maven**

## 📦 Dependencias Principales

- `spring-boot-starter-web` - REST APIs
- `spring-boot-starter-webflux` - WebClient para comunicación entre servicios
- `spring-boot-starter-data-jpa` - Persistencia
- `spring-boot-starter-validation` - Validaciones
- `spring-boot-starter-actuator` - Health checks
- `springdoc-openapi-starter-webmvc-ui` - Documentación Swagger
- `mysql-connector-j` - Driver MySQL
- `jjwt` - JWT

## 🔄 Comunicación entre Microservicios

Los microservicios se comunican mediante:

1. **WebClient** (Spring WebFlux) para llamadas HTTP síncronas
2. **API REST** para comunicación entre servicios
3. Cada servicio valida tokens JWT antes de procesar requests

## 🐛 Solución de Problemas

### Error de conexión a la base de datos

- Verificar que MySQL esté corriendo
- Verificar credenciales en `application.yml`
- Verificar que las bases de datos existan

### Error de puerto en uso

- Cambiar el puerto en `application.yml` de cada servicio
- Actualizar las URLs en `MicroservicesConfig` de los servicios que se comunican

### Error de compilación

- Verificar que JDK 21 esté instalado: `java -version`
- Limpiar y recompilar: `mvn clean install`

## 📄 Licencia

Este proyecto es de uso educativo.

## 👥 Autor

Sistema de Biblioteca Digital - Arquitectura de Microservicios

---

**Nota:** Este es un sistema completo y funcional. Asegúrate de cambiar las contraseñas y secretos JWT en producción.






