# Contexto del Proyecto - EFT

## 📖 Descripción del Contexto

### Sistema de Biblioteca Digital

Este proyecto forma parte de un sistema completo de gestión de biblioteca digital desarrollado con arquitectura de microservicios. La aplicación Android actúa como cliente móvil que se conecta con 5 microservicios backend desarrollados en Spring Boot.

### Objetivo del Proyecto

Desarrollar una aplicación móvil Android que permita a los usuarios:
- Registrarse y autenticarse en el sistema
- Consultar el catálogo de libros disponible
- Realizar préstamos de libros
- Gestionar sus préstamos activos
- Recibir notificaciones sobre sus préstamos
- Visualizar reportes y estadísticas

### Alcance del Proyecto

#### Funcionalidades Implementadas

1. **Gestión de Usuarios**
   - Registro de nuevos usuarios
   - Autenticación con JWT
   - Perfil de usuario
   - Actualización de datos

2. **Catálogo de Libros**
   - Listado de libros con paginación
   - Búsqueda por título, autor o ISBN
   - Filtrado por categoría
   - Verificación de disponibilidad
   - Libros destacados

3. **Gestión de Préstamos**
   - Creación de préstamos
   - Consulta de préstamos activos
   - Devolución de libros
   - Extensión de préstamos
   - Historial de préstamos

4. **Notificaciones**
   - Notificaciones push
   - Alertas de préstamos vencidos
   - Recordatorios de devolución
   - Contador de no leídas

5. **Reportes**
   - Dashboard con estadísticas
   - Reportes personalizados

### Arquitectura del Sistema

```
┌─────────────────────────────────────────┐
│     Aplicación Android (Cliente)       │
│  ┌───────────────────────────────────┐ │
│  │  Presentation Layer (UI/ViewModel)│ │
│  └───────────────────────────────────┘ │
│  ┌───────────────────────────────────┐ │
│  │  Domain Layer (Use Cases)          │ │
│  └───────────────────────────────────┘ │
│  ┌───────────────────────────────────┐ │
│  │  Data Layer (Repository/API)       │ │
│  └───────────────────────────────────┘ │
└─────────────────────────────────────────┘
                    │
                    │ HTTP/REST
                    │
┌─────────────────────────────────────────┐
│   Microservicios Spring Boot (Backend)  │
│  ┌──────────┐ ┌──────────┐ ┌──────────┐│
│  │  User    │ │  Book    │ │  Loan    ││
│  │  Service │ │  Service │ │  Service ││
│  └──────────┘ └──────────┘ └──────────┘│
│  ┌──────────┐ ┌──────────┐            │
│  │ Reports  │ │ Notif.   │            │
│  │ Service  │ │ Service  │            │
│  └──────────┘ └──────────┘            │
└─────────────────────────────────────────┘
```

### Tecnologías y Herramientas

#### Frontend (Android)
- Kotlin
- Android SDK
- Retrofit
- Hilt
- Coroutines
- Room
- Navigation Component

#### Backend (Spring Boot)
- Java 21
- Spring Boot 3.2.0
- Spring Security
- JWT
- MySQL
- WebClient

### Casos de Uso Principales

1. **UC-001: Registro de Usuario**
   - Actor: Usuario nuevo
   - Flujo: Registro → Validación → Activación

2. **UC-002: Autenticación**
   - Actor: Usuario registrado
   - Flujo: Login → Token JWT → Acceso al sistema

3. **UC-003: Consulta de Catálogo**
   - Actor: Usuario autenticado
   - Flujo: Búsqueda → Filtros → Selección

4. **UC-004: Realizar Préstamo**
   - Actor: Usuario autenticado
   - Flujo: Seleccionar libro → Verificar disponibilidad → Crear préstamo

5. **UC-005: Devolver Libro**
   - Actor: Usuario con préstamo activo
   - Flujo: Seleccionar préstamo → Confirmar devolución → Actualizar estado

### Criterios de Aceptación

✅ La aplicación se conecta correctamente con todos los microservicios
✅ La autenticación JWT funciona correctamente
✅ Los datos se muestran correctamente en la UI
✅ Las operaciones CRUD funcionan sin errores
✅ Las notificaciones se reciben en tiempo real
✅ La aplicación maneja errores de red apropiadamente
✅ Los tests unitarios tienen cobertura > 70%
✅ El APK release se genera correctamente firmado

### Entregables

1. ✅ Código fuente completo
2. ✅ APK firmado en modo release
3. ✅ Documentación técnica
4. ✅ Pruebas unitarias con cobertura
5. ✅ README con instrucciones
6. ✅ Diagramas de arquitectura

### Alineación con Parámetros del EFT

- ✅ **Consumo de APIs externas:** Integración con Google Books API
- ✅ **Conexión con microservicios:** 5 servicios Spring Boot
- ✅ **Pruebas unitarias:** Cobertura > 70%
- ✅ **APK firmado:** Generación en modo release
- ✅ **Documentación técnica:** Completa y detallada
- ✅ **Contexto definido:** Sistema de biblioteca digital



