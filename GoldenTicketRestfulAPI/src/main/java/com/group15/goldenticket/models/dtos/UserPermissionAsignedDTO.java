package com.group15.goldenticket.models.dtos;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPermissionAsignedDTO {
	
	private UUID code;
	private String name;
	private String username;
	private String email;
	private Boolean active;
	private List<PermissionVerificationDTO> ListPermission;

}
