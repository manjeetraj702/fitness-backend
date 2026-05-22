package com.fittrack.analytics_core.dto;

import lombok.Data;

@Data
public class AuthRequest {
    private String email;
    private String password;
}