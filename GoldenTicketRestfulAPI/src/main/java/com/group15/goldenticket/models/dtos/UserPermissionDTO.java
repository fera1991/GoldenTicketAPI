package com.group15.goldenticket.models.dtos;


import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPermissionDTO {
    @NotEmpty(message = "user ID is required")
    private String user;
    
    @NotEmpty(message = "permission ID is required")
    private String permission;
}
