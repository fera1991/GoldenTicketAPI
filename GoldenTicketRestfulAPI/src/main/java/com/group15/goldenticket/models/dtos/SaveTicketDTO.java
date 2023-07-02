package com.group15.goldenticket.models.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveTicketDTO {
	@NotEmpty
	private String localityId;
	@NotEmpty
	private String invoiceId;
}
