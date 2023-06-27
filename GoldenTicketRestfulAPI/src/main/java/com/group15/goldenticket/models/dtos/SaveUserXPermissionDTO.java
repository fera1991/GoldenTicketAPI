package com.group15.goldenticket.models.dtos;

import jakarta.validation.constraints.NotEmpty;

public class SaveUserXPermissionDTO {
    @NotEmpty(message = "user ID is required")
    private String user;
    
    @NotEmpty(message = "permission ID is required")
    private String permission;
}
