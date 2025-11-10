package com.ev.auth.dto;

import lombok.Data;
@Data
public class AuthRequest { // Dùng cho Login
    private String username;
    private String password;
}
