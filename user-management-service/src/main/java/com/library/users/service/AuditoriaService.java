package com.library.users.service;

import com.library.users.model.Auditoria;
import com.library.users.repository.AuditoriaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Servicio para gestionar la auditoría del sistema
 * Registra todas las acciones realizadas por usuarios y administradores
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuditoriaService {

    private final AuditoriaRepository auditoriaRepository;

    /**
     * Registrar una acción en el sistema de auditoría
     */
    @Transactional
    public void registrarAccion(Long usuarioId, String nombreUsuario, String tipoEntidad,
                                Long entidadId, Auditoria.Accion accion, String descripcion,
                                String datosAnteriores, String datosNuevos, String ipAddress) {
        try {
            Auditoria auditoria = Auditoria.builder()
                    .usuarioId(usuarioId)
                    .nombreUsuario(nombreUsuario)
                    .tipoEntidad(tipoEntidad)
                    .entidadId(entidadId)
                    .accion(accion)
                    .descripcion(descripcion)
                    .datosAnteriores(datosAnteriores)
                    .datosNuevos(datosNuevos)
                    .ipAddress(ipAddress)
                    .fechaCreacion(LocalDateTime.now())
                    .build();

            auditoriaRepository.save(auditoria);
            log.debug("Acción registrada en auditoría: {} - {} por usuario {}", accion, tipoEntidad, usuarioId);
        } catch (Exception e) {
            log.error("Error al registrar acción en auditoría: {}", e.getMessage());
            // No lanzar excepción para no interrumpir el flujo principal
        }
    }

    /**
     * Obtener todas las auditorías con paginación
     */
    public Page<Auditoria> obtenerTodasLasAuditorias(Pageable pageable) {
        return auditoriaRepository.findAllByOrderByFechaCreacionDesc(pageable);
    }

    /**
     * Obtener auditorías por usuario
     */
    public Page<Auditoria> obtenerAuditoriasPorUsuario(Long usuarioId, Pageable pageable) {
        return auditoriaRepository.findByUsuarioIdOrderByFechaCreacionDesc(usuarioId, pageable);
    }

    /**
     * Obtener auditorías por tipo de entidad
     */
    public Page<Auditoria> obtenerAuditoriasPorTipoEntidad(String tipoEntidad, Pageable pageable) {
        return auditoriaRepository.findByTipoEntidadOrderByFechaCreacionDesc(tipoEntidad, pageable);
    }

    /**
     * Obtener auditorías por acción
     */
    public Page<Auditoria> obtenerAuditoriasPorAccion(Auditoria.Accion accion, Pageable pageable) {
        return auditoriaRepository.findByAccionOrderByFechaCreacionDesc(accion, pageable);
    }

    /**
     * Obtener auditorías por rango de fechas
     */
    public Page<Auditoria> obtenerAuditoriasPorFecha(LocalDateTime fechaInicio, LocalDateTime fechaFin, Pageable pageable) {
        return auditoriaRepository.findByFechaCreacionBetween(fechaInicio, fechaFin, pageable);
    }

    /**
     * Obtener historial de cambios de una entidad específica
     */
    public Page<Auditoria> obtenerHistorialEntidad(String tipoEntidad, Long entidadId, Pageable pageable) {
        return auditoriaRepository.findByTipoEntidadAndEntidadIdOrderByFechaCreacionDesc(tipoEntidad, entidadId, pageable);
    }

    /**
     * Obtener estadísticas de acciones
     */
    public List<Object[]> obtenerEstadisticasAcciones() {
        return auditoriaRepository.contarAccionesPorTipo();
    }
}


