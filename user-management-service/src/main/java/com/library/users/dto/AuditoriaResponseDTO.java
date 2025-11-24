package com.library.users.dto;

import com.library.users.model.Auditoria;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para respuesta de auditoría
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditoriaResponseDTO {

    private Long id;
    private Long usuarioId;
    private String nombreUsuario;
    private String tipoEntidad;
    private Long entidadId;
    private Auditoria.Accion accion;
    private String descripcion;
    private String datosAnteriores;
    private String datosNuevos;
    private String ipAddress;
    private LocalDateTime fechaCreacion;

    public static AuditoriaResponseDTO fromEntity(Auditoria auditoria) {
        return AuditoriaResponseDTO.builder()
                .id(auditoria.getId())
                .usuarioId(auditoria.getUsuarioId())
                .nombreUsuario(auditoria.getNombreUsuario())
                .tipoEntidad(auditoria.getTipoEntidad())
                .entidadId(auditoria.getEntidadId())
                .accion(auditoria.getAccion())
                .descripcion(auditoria.getDescripcion())
                .datosAnteriores(auditoria.getDatosAnteriores())
                .datosNuevos(auditoria.getDatosNuevos())
                .ipAddress(auditoria.getIpAddress())
                .fechaCreacion(auditoria.getFechaCreacion())
                .build();
    }
}


