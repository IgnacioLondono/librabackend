package com.library.users.dto;

import com.library.users.model.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para cambiar el rol de un usuario")
public class ChangeRoleDTO {

    @NotNull(message = "El rol es obligatorio")
    @Schema(description = "Nuevo rol del usuario", example = "ADMINISTRADOR", allowableValues = {"USUARIO", "ADMINISTRADOR"}, requiredMode = Schema.RequiredMode.REQUIRED)
    private User.Role role;
}


