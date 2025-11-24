package com.library.users.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlockUserDTO {

    @NotNull(message = "El campo 'blocked' es obligatorio")
    private Boolean blocked;
}


