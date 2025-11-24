# 📋 Endpoints de Administración - Auditoría

## 🔐 Autenticación

Todos los endpoints de auditoría requieren:
- **Autenticación JWT**: Token Bearer en el header `Authorization`
- **Rol Administrador**: Solo usuarios con rol `ADMINISTRADOR` pueden acceder

### Ejemplo de Header:
```
Authorization: Bearer <token_jwt>
```

---

## 📍 Base URL
```
/api/admin/auditoria
```

---

## 📊 Endpoints Disponibles

### 1. Listar Todas las Auditorías

**GET** `/api/admin/auditoria`

Obtiene una lista paginada de todos los registros de auditoría del sistema.

**Parámetros de Query:**
- `page` (int, opcional): Número de página (default: 0)
- `size` (int, opcional): Tamaño de página (default: 20)

**Ejemplo de Request:**
```http
GET /api/admin/auditoria?page=0&size=20
Authorization: Bearer <token>
```

**Ejemplo de Response:**
```json
{
  "content": [
    {
      "id": 1,
      "usuarioId": 5,
      "nombreUsuario": "admin@example.com",
      "tipoEntidad": "USUARIO",
      "entidadId": 10,
      "accion": "BLOQUEAR",
      "descripcion": "Usuario bloqueado por el administrador",
      "datosAnteriores": "{\"status\":\"ACTIVO\"}",
      "datosNuevos": "{\"status\":\"BLOQUEADO\"}",
      "ipAddress": "192.168.1.100",
      "fechaCreacion": "2024-01-15T10:30:00"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 20
  },
  "totalElements": 150,
  "totalPages": 8
}
```

---

### 2. Auditorías por Usuario

**GET** `/api/admin/auditoria/usuario/{usuarioId}`

Obtiene todas las auditorías realizadas por un usuario específico.

**Parámetros:**
- `usuarioId` (Long, requerido): ID del usuario

**Parámetros de Query:**
- `page` (int, opcional): Número de página (default: 0)
- `size` (int, opcional): Tamaño de página (default: 20)

**Ejemplo de Request:**
```http
GET /api/admin/auditoria/usuario/5?page=0&size=20
Authorization: Bearer <token>
```

---

### 3. Auditorías por Tipo de Entidad

**GET** `/api/admin/auditoria/tipo-entidad/{tipoEntidad}`

Obtiene todas las auditorías de un tipo de entidad específico.

**Tipos de Entidad válidos:**
- `USUARIO`
- `LIBRO`
- `PRESTAMO`
- `NOTIFICACION`

**Parámetros:**
- `tipoEntidad` (String, requerido): Tipo de entidad

**Parámetros de Query:**
- `page` (int, opcional): Número de página (default: 0)
- `size` (int, opcional): Tamaño de página (default: 20)

**Ejemplo de Request:**
```http
GET /api/admin/auditoria/tipo-entidad/USUARIO?page=0&size=20
Authorization: Bearer <token>
```

---

### 4. Auditorías por Acción

**GET** `/api/admin/auditoria/accion/{accion}`

Obtiene todas las auditorías de un tipo de acción específico.

**Tipos de Acción válidos:**
- `CREAR`
- `ACTUALIZAR`
- `ELIMINAR`
- `BLOQUEAR`
- `DESBLOQUEAR`
- `CAMBIAR_ROL`
- `INICIAR_SESION`
- `CERRAR_SESION`
- `CREAR_PRESTAMO`
- `DEVOLVER_PRESTAMO`
- `EXTENDER_PRESTAMO`
- `CANCELAR_PRESTAMO`
- `CREAR_LIBRO`
- `ACTUALIZAR_LIBRO`
- `ELIMINAR_LIBRO`
- `ACTUALIZAR_COPIAS`

**Parámetros:**
- `accion` (String, requerido): Tipo de acción (case-insensitive)

**Parámetros de Query:**
- `page` (int, opcional): Número de página (default: 0)
- `size` (int, opcional): Tamaño de página (default: 20)

**Ejemplo de Request:**
```http
GET /api/admin/auditoria/accion/BLOQUEAR?page=0&size=20
Authorization: Bearer <token>
```

---

### 5. Auditorías por Rango de Fechas

**GET** `/api/admin/auditoria/fechas`

Obtiene todas las auditorías dentro de un rango de fechas.

**Parámetros de Query:**
- `fechaInicio` (LocalDateTime, requerido): Fecha de inicio (formato: `yyyy-MM-ddTHH:mm:ss`)
- `fechaFin` (LocalDateTime, requerido): Fecha de fin (formato: `yyyy-MM-ddTHH:mm:ss`)
- `page` (int, opcional): Número de página (default: 0)
- `size` (int, opcional): Tamaño de página (default: 20)

**Ejemplo de Request:**
```http
GET /api/admin/auditoria/fechas?fechaInicio=2024-01-01T00:00:00&fechaFin=2024-01-31T23:59:59&page=0&size=20
Authorization: Bearer <token>
```

---

### 6. Historial de una Entidad Específica

**GET** `/api/admin/auditoria/entidad/{tipoEntidad}/{entidadId}`

Obtiene el historial completo de cambios de una entidad específica.

**Parámetros:**
- `tipoEntidad` (String, requerido): Tipo de entidad
- `entidadId` (Long, requerido): ID de la entidad

**Parámetros de Query:**
- `page` (int, opcional): Número de página (default: 0)
- `size` (int, opcional): Tamaño de página (default: 20)

**Ejemplo de Request:**
```http
GET /api/admin/auditoria/entidad/USUARIO/10?page=0&size=20
Authorization: Bearer <token>
```

**Ejemplo de Response:**
```json
{
  "content": [
    {
      "id": 1,
      "usuarioId": 5,
      "nombreUsuario": "admin@example.com",
      "tipoEntidad": "USUARIO",
      "entidadId": 10,
      "accion": "CREAR",
      "descripcion": "Usuario creado",
      "datosAnteriores": null,
      "datosNuevos": "{\"name\":\"Juan Pérez\",\"email\":\"juan@example.com\"}",
      "ipAddress": "192.168.1.100",
      "fechaCreacion": "2024-01-10T08:00:00"
    },
    {
      "id": 2,
      "usuarioId": 5,
      "nombreUsuario": "admin@example.com",
      "tipoEntidad": "USUARIO",
      "entidadId": 10,
      "accion": "ACTUALIZAR",
      "descripcion": "Usuario actualizado",
      "datosAnteriores": "{\"phone\":null}",
      "datosNuevos": "{\"phone\":\"+56912345678\"}",
      "ipAddress": "192.168.1.100",
      "fechaCreacion": "2024-01-12T14:30:00"
    }
  ]
}
```

---

### 7. Estadísticas de Acciones

**GET** `/api/admin/auditoria/estadisticas`

Obtiene estadísticas de acciones realizadas en el sistema, agrupadas por tipo de acción.

**Ejemplo de Request:**
```http
GET /api/admin/auditoria/estadisticas
Authorization: Bearer <token>
```

**Ejemplo de Response:**
```json
{
  "CREAR": 45,
  "ACTUALIZAR": 120,
  "ELIMINAR": 8,
  "BLOQUEAR": 5,
  "DESBLOQUEAR": 2,
  "CAMBIAR_ROL": 3,
  "INICIAR_SESION": 500,
  "CERRAR_SESION": 480,
  "CREAR_PRESTAMO": 150,
  "DEVOLVER_PRESTAMO": 140,
  "EXTENDER_PRESTAMO": 25,
  "CANCELAR_PRESTAMO": 10,
  "CREAR_LIBRO": 30,
  "ACTUALIZAR_LIBRO": 50,
  "ELIMINAR_LIBRO": 5,
  "ACTUALIZAR_COPIAS": 80
}
```

---

### 8. Búsqueda Avanzada

**GET** `/api/admin/auditoria/buscar`

Búsqueda avanzada con múltiples filtros opcionales. Si se proporcionan múltiples filtros, se aplica el primero en el siguiente orden:
1. `usuarioId`
2. `tipoEntidad`
3. `accion`
4. `fechaInicio` y `fechaFin` (ambos requeridos)

**Parámetros de Query (todos opcionales):**
- `usuarioId` (Long): ID del usuario
- `tipoEntidad` (String): Tipo de entidad
- `accion` (String): Tipo de acción
- `fechaInicio` (LocalDateTime): Fecha de inicio
- `fechaFin` (LocalDateTime): Fecha de fin
- `page` (int, opcional): Número de página (default: 0)
- `size` (int, opcional): Tamaño de página (default: 20)

**Ejemplo de Request:**
```http
GET /api/admin/auditoria/buscar?tipoEntidad=USUARIO&accion=BLOQUEAR&page=0&size=20
Authorization: Bearer <token>
```

---

## 🔒 Códigos de Respuesta

- **200 OK**: Solicitud exitosa
- **400 Bad Request**: Parámetros inválidos (ej: acción no válida)
- **403 Forbidden**: Usuario no tiene permisos de administrador
- **401 Unauthorized**: Token JWT inválido o expirado

---

## 📝 Notas Importantes

1. **Paginación**: Todos los endpoints que retornan listas usan paginación. El tamaño máximo recomendado es 100 elementos por página.

2. **Ordenamiento**: Todos los resultados están ordenados por fecha de creación descendente (más recientes primero).

3. **Filtros Combinados**: El endpoint de búsqueda avanzada aplica solo un filtro a la vez según el orden de prioridad mencionado.

4. **Formato de Fechas**: Las fechas deben estar en formato ISO 8601: `yyyy-MM-ddTHH:mm:ss` (ejemplo: `2024-01-15T10:30:00`)

5. **Datos JSON**: Los campos `datosAnteriores` y `datosNuevos` contienen JSON strings que pueden ser parseados para ver los cambios detallados.

---

## 🧪 Ejemplos de Uso con cURL

### Listar todas las auditorías
```bash
curl -X GET "http://localhost:8081/api/admin/auditoria?page=0&size=20" \
  -H "Authorization: Bearer <tu_token_jwt>"
```

### Buscar auditorías de un usuario
```bash
curl -X GET "http://localhost:8081/api/admin/auditoria/usuario/5?page=0&size=20" \
  -H "Authorization: Bearer <tu_token_jwt>"
```

### Buscar por tipo de entidad
```bash
curl -X GET "http://localhost:8081/api/admin/auditoria/tipo-entidad/USUARIO?page=0&size=20" \
  -H "Authorization: Bearer <tu_token_jwt>"
```

### Buscar por rango de fechas
```bash
curl -X GET "http://localhost:8081/api/admin/auditoria/fechas?fechaInicio=2024-01-01T00:00:00&fechaFin=2024-01-31T23:59:59&page=0&size=20" \
  -H "Authorization: Bearer <tu_token_jwt>"
```

### Obtener estadísticas
```bash
curl -X GET "http://localhost:8081/api/admin/auditoria/estadisticas" \
  -H "Authorization: Bearer <tu_token_jwt>"
```

---

**Fecha de creación:** 2024-01-15
**Versión:** 1.0

