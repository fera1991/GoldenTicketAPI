package com.group15.goldenticket.models.dtos;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveEventDTO {
	@NotEmpty
	private String categoryId;
	@NotEmpty
	private String place;
	@NotEmpty
	private String tittle;
	
	@NotEmpty
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	private String date;
	@NotNull
	private Integer capacity;
	@NotEmpty
	private String involved;
	
	@NotEmpty
	private String image;
	
	@NotNull
	private Integer duration;
	
	@NotEmpty
	private String sponsors;
}
