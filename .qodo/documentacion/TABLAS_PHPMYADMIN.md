# Nombres de Tablas para phpMyAdmin

## 📋 Tablas por Base de Datos

### 1️⃣ Base de Datos: `library_users_db`
**Microservicio: User Management Service**

- **`users`**
- **`sessions`**

---

### 2️⃣ Base de Datos: `library_books_db`
**Microservicio: Book Catalog Service**

- **`books`**

---

### 3️⃣ Base de Datos: `library_loans_db`
**Microservicio: Loan Management Service**

- **`loans`**
- **`loan_history`**

---

### 4️⃣ Base de Datos: `library_reports_db`
**Microservicio: Reports Service**

- **`reports`**

---

### 5️⃣ Base de Datos: `library_notifications_db`
**Microservicio: Notifications Service**

- **`notifications`**

---

## 📝 Instrucciones

1. **Crea las bases de datos** en phpMyAdmin:
   - `library_users_db`
   - `library_books_db`
   - `library_loans_db`
   - `library_reports_db`
   - `library_notifications_db`

2. **Dentro de cada base de datos, crea las tablas** con los nombres exactos indicados arriba.

3. **Las tablas pueden estar vacías** - Spring Boot con `ddl-auto: update` creará las columnas automáticamente cuando ejecutes los microservicios.

4. **Importante:** Usa los nombres **exactos** indicados arriba (en minúsculas).

---

## ✅ Resumen Total

**Total de tablas:** 7 tablas distribuidas en 5 bases de datos

- `library_users_db` → 2 tablas
- `library_books_db` → 1 tabla
- `library_loans_db` → 2 tablas
- `library_reports_db` → 1 tabla
- `library_notifications_db` → 1 tabla




