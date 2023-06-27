package com.group15.goldenticket.models.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveLocalityDTO {
	@NotEmpty
	private String eventId;
	@NotEmpty
	private String name;
	@NotNull
	private Float price;
	@NotNull
	private Integer availableQuantity;
}
