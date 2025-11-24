package com.library.users.repository;

import com.library.users.model.Auditoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio para gestionar los registros de auditoría
 */
@Repository
public interface AuditoriaRepository extends JpaRepository<Auditoria, Long> {

    /**
     * Buscar auditoría por usuario
     */
    Page<Auditoria> findByUsuarioIdOrderByFechaCreacionDesc(Long usuarioId, Pageable pageable);

    /**
     * Buscar auditoría por tipo de entidad
     */
    Page<Auditoria> findByTipoEntidadOrderByFechaCreacionDesc(String tipoEntidad, Pageable pageable);

    /**
     * Buscar auditoría por acción
     */
    Page<Auditoria> findByAccionOrderByFechaCreacionDesc(Auditoria.Accion accion, Pageable pageable);

    /**
     * Buscar auditoría por rango de fechas
     */
    @Query("SELECT a FROM Auditoria a WHERE a.fechaCreacion BETWEEN :fechaInicio AND :fechaFin ORDER BY a.fechaCreacion DESC")
    Page<Auditoria> findByFechaCreacionBetween(
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin,
            Pageable pageable);

    /**
     * Buscar auditoría por entidad específica
     */
    Page<Auditoria> findByTipoEntidadAndEntidadIdOrderByFechaCreacionDesc(
            String tipoEntidad, Long entidadId, Pageable pageable);

    /**
     * Obtener todas las auditorías recientes
     */
    Page<Auditoria> findAllByOrderByFechaCreacionDesc(Pageable pageable);

    /**
     * Contar acciones por tipo
     */
    @Query("SELECT a.accion, COUNT(a) FROM Auditoria a GROUP BY a.accion")
    List<Object[]> contarAccionesPorTipo();
}


