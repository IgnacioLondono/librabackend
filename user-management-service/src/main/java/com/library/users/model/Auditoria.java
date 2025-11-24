package com.library.users.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Modelo de Auditoría para registrar todos los cambios realizados en el sistema
 * Permite al administrador ver quién hizo qué y cuándo
 */
@Entity
@Table(name = "auditoria")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Auditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId; // Usuario que realizó la acción

    @Column(name = "nombre_usuario", length = 100)
    private String nombreUsuario; // Nombre del usuario para facilitar búsquedas

    @Column(name = "tipo_entidad", nullable = false, length = 50)
    private String tipoEntidad; // USUARIO, LIBRO, PRESTAMO, etc.

    @Column(name = "entidad_id")
    private Long entidadId; // ID de la entidad afectada

    @Enumerated(EnumType.STRING)
    @Column(name = "accion", nullable = false, length = 50)
    private Accion accion; // CREAR, ACTUALIZAR, ELIMINAR, BLOQUEAR, etc.

    @Column(name = "descripcion", length = 500)
    private String descripcion; // Descripción detallada de la acción

    @Column(name = "datos_anteriores", columnDefinition = "TEXT")
    private String datosAnteriores; // JSON con los datos antes del cambio

    @Column(name = "datos_nuevos", columnDefinition = "TEXT")
    private String datosNuevos; // JSON con los datos después del cambio

    @Column(name = "ip_address", length = 50)
    private String ipAddress; // IP desde donde se realizó la acción

    @CreatedDate
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    public enum Accion {
        CREAR,
        ACTUALIZAR,
        ELIMINAR,
        BLOQUEAR,
        DESBLOQUEAR,
        CAMBIAR_ROL,
        INICIAR_SESION,
        CERRAR_SESION,
        CREAR_PRESTAMO,
        DEVOLVER_PRESTAMO,
        EXTENDER_PRESTAMO,
        CANCELAR_PRESTAMO,
        CREAR_LIBRO,
        ACTUALIZAR_LIBRO,
        ELIMINAR_LIBRO,
        ACTUALIZAR_COPIAS
    }
}


