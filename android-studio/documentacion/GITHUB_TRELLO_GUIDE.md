# 📋 Guía de Colaboración - GitHub y Trello

## 🎯 Requisito: Evidencia de Trabajo Colaborativo

Debes demostrar:
- ✅ Actividad técnica real en GitHub
- ✅ Planificación en Trello
- ✅ Commits por integrante
- ✅ Trabajo colaborativo

## 📦 GitHub - Configuración

### 1. Estructura del Repositorio

```
tu-repositorio/
├── app/                    # Código Android
├── backend/                # Microservicios Spring Boot
│   ├── user-management-service/
│   ├── book-catalog-service/
│   ├── loan-management-service/
│   ├── reports-service/
│   └── notifications-service/
├── docs/                   # Documentación
├── postman/                # Colección Postman
├── .gitignore
└── README.md
```

### 2. Commits Significativos

**Buenos ejemplos de commits:**
```
feat: Agregar autenticación JWT en LoginViewModel
fix: Corregir error de navegación en BookListFragment
test: Agregar tests unitarios para LoanRepository
docs: Actualizar README con instrucciones de instalación
refactor: Mejorar estructura de paquetes en data layer
```

**Evitar:**
```
- "cambios"
- "fix"
- "update"
- "commit"
```

### 3. Branches

```
main                    # Código estable
develop                 # Desarrollo activo
feature/login           # Feature: Login
feature/books           # Feature: Catálogo
feature/loans           # Feature: Préstamos
fix/navigation-bug      # Fix: Bug de navegación
```

### 4. Pull Requests

- Título descriptivo
- Descripción de cambios
- Checklist de revisión
- Screenshots si aplica

## 📋 Trello - Planificación

### Estructura de Tablero

```
📋 Backlog
📋 To Do
📋 In Progress
📋 Review
📋 Done
```

### Tarjetas por Requisito

**Tarjeta: "Interfaz Visual - Jetpack Compose"**
- [ ] Pantalla de Login
- [ ] Pantalla de Registro
- [ ] Lista de Libros
- [ ] Detalle de Libro
- [ ] Lista de Préstamos
- [ ] Pantalla de Notificaciones

**Tarjeta: "Integración Microservicios"**
- [ ] User Management Service
- [ ] Book Catalog Service
- [ ] Loan Management Service
- [ ] Reports Service
- [ ] Notifications Service

**Tarjeta: "API Externa"**
- [ ] Configurar Google Books API
- [ ] Integrar en UI
- [ ] Testear funcionalidad

**Tarjeta: "Pruebas Unitarias"**
- [ ] Tests ViewModels
- [ ] Tests Repositories
- [ ] Tests Use Cases
- [ ] Verificar cobertura > 80%

**Tarjeta: "APK Firmado"**
- [ ] Generar keystore
- [ ] Configurar signing
- [ ] Generar APK release
- [ ] Probar APK

**Tarjeta: "Documentación"**
- [ ] README completo
- [ ] Documentación de APIs
- [ ] Diagramas de arquitectura

### Asignación de Tareas

- Asignar cada tarjeta a un integrante
- Usar etiquetas por tipo (Frontend, Backend, Testing, Docs)
- Fechas de vencimiento
- Comentarios con progreso

## 📊 Evidencia de Trabajo

### Commits por Integrante

**Integrante 1:**
```
feat: Implementar LoginViewModel con autenticación JWT
feat: Agregar BookListFragment con paginación
test: Tests unitarios para UserRepository
fix: Corregir error de navegación en BookDetail
```

**Integrante 2:**
```
feat: Configurar Retrofit para microservicios
feat: Implementar LoanViewModel y casos de uso
feat: Integrar Google Books API
docs: Actualizar documentación técnica
```

### Actividad en GitHub

- Commits diarios o cada 2 días
- Pull requests con descripciones
- Issues para bugs encontrados
- Releases con tags (v1.0.0)

### Actividad en Trello

- Tarjetas movidas regularmente
- Comentarios con actualizaciones
- Archivos adjuntos (screenshots, documentos)
- Checklist completados

## ✅ Checklist de Entrega

- [ ] Repositorio GitHub público o con acceso
- [ ] README completo y actualizado
- [ ] Commits significativos y descriptivos
- [ ] Commits de ambos integrantes visibles
- [ ] Trello con planificación completa
- [ ] Tarjetas organizadas y actualizadas
- [ ] Evidencia de trabajo colaborativo
- [ ] APK en releases o carpeta releases/

## 📝 Ejemplo de README para GitHub

```markdown
# Sistema de Biblioteca Digital - Android

## 👥 Integrantes
- [Nombre 1] - Frontend, Testing
- [Nombre 2] - Backend, Integración APIs

## 📋 Descripción
Aplicación Android que se conecta con microservicios Spring Boot...

## 🛠️ Tecnologías
- Kotlin
- Jetpack Compose
- Retrofit
- Hilt
- Coroutines

## 📦 Instalación
[Instrucciones]

## 🧪 Tests
```bash
./gradlew test
```

## 📱 APK
Descargar desde [Releases](releases/)

## 📊 Contribuciones
Ver commits y pull requests para detalles de implementación.
```

