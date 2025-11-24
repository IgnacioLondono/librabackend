package com.library.users.dto;

import com.library.users.model.User;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeRoleDTO {

    @NotNull(message = "El rol es obligatorio")
    private User.Role role;
}


