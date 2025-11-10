package com.ev.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class AuthResponse { // Trả về cho client
    private Long accountId;
    private String username;
    private String token; // JWT Token
}
