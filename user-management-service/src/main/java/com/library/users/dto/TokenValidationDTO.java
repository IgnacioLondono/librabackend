package com.library.users.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenValidationDTO {

    private String token;
    private boolean valid;
    private Long userId;
    private String message;
}






