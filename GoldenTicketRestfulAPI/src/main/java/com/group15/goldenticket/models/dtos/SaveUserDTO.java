package com.group15.goldenticket.models.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveUserDTO {
	private String name;
	private String username;
	@NotEmpty
	private String email;
	private String password;
}
