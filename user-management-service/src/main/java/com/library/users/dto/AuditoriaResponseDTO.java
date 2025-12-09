package com.library.users.dto;

import com.library.users.model.Auditoria;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "DTO de respuesta con información de un registro de auditoría")
public class AuditoriaResponseDTO {

    @Schema(description = "ID único del registro de auditoría", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Schema(description = "ID del usuario que realizó la acción", example = "5", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long usuarioId;

    @Schema(description = "Nombre del usuario que realizó la acción", example = "Juan Pérez", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombreUsuario;

    @Schema(description = "Tipo de entidad afectada", example = "USER", requiredMode = Schema.RequiredMode.REQUIRED)
    private String tipoEntidad;

    @Schema(description = "ID de la entidad afectada", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long entidadId;

    @Schema(description = "Acción realizada", example = "UPDATE",
            allowableValues = {"CREATE", "UPDATE", "DELETE", "LOGIN", "LOGOUT", "BLOCK", "UNBLOCK"},
            requiredMode = Schema.RequiredMode.REQUIRED)
    private Auditoria.Accion accion;

    @Schema(description = "Descripción detallada de la acción", example = "Usuario actualizado", requiredMode = Schema.RequiredMode.REQUIRED)
    private String descripcion;

    @Schema(description = "Datos anteriores (JSON)", example = "{\"name\":\"Juan\",\"email\":\"juan@example.com\"}")
    private String datosAnteriores;

    @Schema(description = "Datos nuevos (JSON)", example = "{\"name\":\"Juan Carlos\",\"email\":\"juan@example.com\"}")
    private String datosNuevos;

    @Schema(description = "Dirección IP desde donde se realizó la acción", example = "192.168.1.100")
    private String ipAddress;

    @Schema(description = "Fecha y hora de la acción", example = "2024-01-15T10:30:00", requiredMode = Schema.RequiredMode.REQUIRED)
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


