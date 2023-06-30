package com.group15.goldenticket.models.dtos;

import java.util.Date;
import java.util.UUID;

import com.group15.goldenticket.models.entities.Locality;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ShowTicketDTO {
	
	private UUID code;
	private ShowUserDTO user;
	private Locality locality;
	private Date purchaseDate;
	
}
