package com.library.users.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para bloquear o desbloquear un usuario")
public class BlockUserDTO {

    @NotNull(message = "El campo 'blocked' es obligatorio")
    @Schema(description = "true para bloquear el usuario, false para desbloquearlo. Al bloquear, se invalidan todas sus sesiones activas.", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean blocked;
}


