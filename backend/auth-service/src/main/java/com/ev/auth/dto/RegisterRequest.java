package com.ev.auth.dto;

import com.ev.auth.entity.Role;
import lombok.Data;
@Data
public class RegisterRequest {
    private String username;
    private String password;
    private Role role;
}
