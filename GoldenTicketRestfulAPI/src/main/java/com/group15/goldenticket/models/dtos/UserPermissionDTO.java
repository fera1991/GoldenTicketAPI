package com.group15.goldenticket.models.dtos;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPermissionDTO {
    @NotNull(message = "user ID is required")
    private String user;
    
    @NotNull(message = "permission ID is required")
    private String permission;
}
