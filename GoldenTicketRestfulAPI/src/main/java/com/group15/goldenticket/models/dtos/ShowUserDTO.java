package com.group15.goldenticket.models.dtos;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ShowUserDTO {
	
	private UUID code;
	private String username;
	private String email;
}
