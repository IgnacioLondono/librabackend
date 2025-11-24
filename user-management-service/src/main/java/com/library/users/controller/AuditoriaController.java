package com.library.users.controller;

import com.library.users.dto.AuditoriaResponseDTO;
import com.library.users.model.Auditoria;
import com.library.users.security.JwtUtil;
import com.library.users.service.AuditoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controlador para gestión de auditoría
 * Solo accesible para administradores
 */
@RestController
@RequestMapping("/api/admin/auditoria")
@RequiredArgsConstructor
@Tag(name = "Auditoría", description = "API para consultar registros de auditoría (Solo Administradores)")
@SecurityRequirement(name = "bearerAuth")
public class AuditoriaController {

    private final AuditoriaService auditoriaService;
    private final JwtUtil jwtUtil;

    /**
     * Verificar si el usuario es administrador
     */
    private boolean isAdmin(HttpServletRequest request) {
        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                String role = jwtUtil.extractRole(token);
                return "ADMINISTRADOR".equals(role);
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    @GetMapping
    @Operation(summary = "Listar todas las auditorías", 
               description = "Obtiene una lista paginada de todos los registros de auditoría. Solo para administradores.")
    public ResponseEntity<Page<AuditoriaResponseDTO>> getAllAuditorias(
            @Parameter(description = "Número de página") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamaño de página") @RequestParam(defaultValue = "20") int size,
            HttpServletRequest request) {
        
        if (!isAdmin(request)) {
            return ResponseEntity.status(403).build();
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Auditoria> auditorias = auditoriaService.obtenerTodasLasAuditorias(pageable);
        Page<AuditoriaResponseDTO> response = auditorias.map(AuditoriaResponseDTO::fromEntity);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Auditorías por usuario", 
               description = "Obtiene todas las auditorías realizadas por un usuario específico. Solo para administradores.")
    public ResponseEntity<Page<AuditoriaResponseDTO>> getAuditoriasPorUsuario(
            @Parameter(description = "ID del usuario") @PathVariable Long usuarioId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            HttpServletRequest request) {
        
        if (!isAdmin(request)) {
            return ResponseEntity.status(403).build();
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Auditoria> auditorias = auditoriaService.obtenerAuditoriasPorUsuario(usuarioId, pageable);
        Page<AuditoriaResponseDTO> response = auditorias.map(AuditoriaResponseDTO::fromEntity);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/tipo-entidad/{tipoEntidad}")
    @Operation(summary = "Auditorías por tipo de entidad", 
               description = "Obtiene todas las auditorías de un tipo de entidad específico (USUARIO, LIBRO, PRESTAMO, etc.). Solo para administradores.")
    public ResponseEntity<Page<AuditoriaResponseDTO>> getAuditoriasPorTipoEntidad(
            @Parameter(description = "Tipo de entidad (USUARIO, LIBRO, PRESTAMO, etc.)") @PathVariable String tipoEntidad,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            HttpServletRequest request) {
        
        if (!isAdmin(request)) {
            return ResponseEntity.status(403).build();
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Auditoria> auditorias = auditoriaService.obtenerAuditoriasPorTipoEntidad(tipoEntidad, pageable);
        Page<AuditoriaResponseDTO> response = auditorias.map(AuditoriaResponseDTO::fromEntity);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/accion/{accion}")
    @Operation(summary = "Auditorías por acción", 
               description = "Obtiene todas las auditorías de un tipo de acción específico (CREAR, ACTUALIZAR, ELIMINAR, etc.). Solo para administradores.")
    public ResponseEntity<Page<AuditoriaResponseDTO>> getAuditoriasPorAccion(
            @Parameter(description = "Tipo de acción (CREAR, ACTUALIZAR, ELIMINAR, BLOQUEAR, etc.)") @PathVariable String accion,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            HttpServletRequest request) {
        
        if (!isAdmin(request)) {
            return ResponseEntity.status(403).build();
        }

        try {
            Auditoria.Accion accionEnum = Auditoria.Accion.valueOf(accion.toUpperCase());
            Pageable pageable = PageRequest.of(page, size);
            Page<Auditoria> auditorias = auditoriaService.obtenerAuditoriasPorAccion(accionEnum, pageable);
            Page<AuditoriaResponseDTO> response = auditorias.map(AuditoriaResponseDTO::fromEntity);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/fechas")
    @Operation(summary = "Auditorías por rango de fechas", 
               description = "Obtiene todas las auditorías dentro de un rango de fechas. Solo para administradores.")
    public ResponseEntity<Page<AuditoriaResponseDTO>> getAuditoriasPorFechas(
            @Parameter(description = "Fecha de inicio (formato: yyyy-MM-ddTHH:mm:ss)") 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @Parameter(description = "Fecha de fin (formato: yyyy-MM-ddTHH:mm:ss)") 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            HttpServletRequest request) {
        
        if (!isAdmin(request)) {
            return ResponseEntity.status(403).build();
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Auditoria> auditorias = auditoriaService.obtenerAuditoriasPorFecha(fechaInicio, fechaFin, pageable);
        Page<AuditoriaResponseDTO> response = auditorias.map(AuditoriaResponseDTO::fromEntity);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/entidad/{tipoEntidad}/{entidadId}")
    @Operation(summary = "Historial de una entidad específica", 
               description = "Obtiene el historial completo de cambios de una entidad específica. Solo para administradores.")
    public ResponseEntity<Page<AuditoriaResponseDTO>> getHistorialEntidad(
            @Parameter(description = "Tipo de entidad (USUARIO, LIBRO, PRESTAMO, etc.)") @PathVariable String tipoEntidad,
            @Parameter(description = "ID de la entidad") @PathVariable Long entidadId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            HttpServletRequest request) {
        
        if (!isAdmin(request)) {
            return ResponseEntity.status(403).build();
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Auditoria> auditorias = auditoriaService.obtenerHistorialEntidad(tipoEntidad, entidadId, pageable);
        Page<AuditoriaResponseDTO> response = auditorias.map(AuditoriaResponseDTO::fromEntity);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/estadisticas")
    @Operation(summary = "Estadísticas de acciones", 
               description = "Obtiene estadísticas de acciones realizadas en el sistema. Solo para administradores.")
    public ResponseEntity<Map<String, Long>> getEstadisticasAcciones(HttpServletRequest request) {
        
        if (!isAdmin(request)) {
            return ResponseEntity.status(403).build();
        }

        List<Object[]> estadisticas = auditoriaService.obtenerEstadisticasAcciones();
        Map<String, Long> resultado = estadisticas.stream()
                .collect(Collectors.toMap(
                        obj -> ((Auditoria.Accion) obj[0]).name(),
                        obj -> ((Number) obj[1]).longValue()
                ));

        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/buscar")
    @Operation(summary = "Búsqueda avanzada de auditorías", 
               description = "Búsqueda avanzada con múltiples filtros opcionales. Solo para administradores.")
    public ResponseEntity<Page<AuditoriaResponseDTO>> buscarAuditorias(
            @Parameter(description = "ID del usuario (opcional)") @RequestParam(required = false) Long usuarioId,
            @Parameter(description = "Tipo de entidad (opcional)") @RequestParam(required = false) String tipoEntidad,
            @Parameter(description = "Tipo de acción (opcional)") @RequestParam(required = false) String accion,
            @Parameter(description = "Fecha de inicio (opcional)") 
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @Parameter(description = "Fecha de fin (opcional)") 
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            HttpServletRequest request) {
        
        if (!isAdmin(request)) {
            return ResponseEntity.status(403).build();
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Auditoria> auditorias;

        // Aplicar filtros según los parámetros proporcionados
        if (usuarioId != null) {
            auditorias = auditoriaService.obtenerAuditoriasPorUsuario(usuarioId, pageable);
        } else if (tipoEntidad != null) {
            auditorias = auditoriaService.obtenerAuditoriasPorTipoEntidad(tipoEntidad, pageable);
        } else if (accion != null) {
            try {
                Auditoria.Accion accionEnum = Auditoria.Accion.valueOf(accion.toUpperCase());
                auditorias = auditoriaService.obtenerAuditoriasPorAccion(accionEnum, pageable);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().build();
            }
        } else if (fechaInicio != null && fechaFin != null) {
            auditorias = auditoriaService.obtenerAuditoriasPorFecha(fechaInicio, fechaFin, pageable);
        } else {
            // Si no hay filtros, retornar todas
            auditorias = auditoriaService.obtenerTodasLasAuditorias(pageable);
        }

        Page<AuditoriaResponseDTO> response = auditorias.map(AuditoriaResponseDTO::fromEntity);
        return ResponseEntity.ok(response);
    }
}

