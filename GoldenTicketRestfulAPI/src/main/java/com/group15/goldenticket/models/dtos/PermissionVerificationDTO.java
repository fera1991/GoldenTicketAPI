package com.group15.goldenticket.models.dtos;

import java.util.UUID;

import com.group15.goldenticket.models.entities.Permission;
import com.group15.goldenticket.models.entities.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PermissionVerificationDTO {
	private UUID code;
	private Permission permission;
	private Boolean Status; 
}
