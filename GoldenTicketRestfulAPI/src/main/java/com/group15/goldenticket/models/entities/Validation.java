package com.group15.goldenticket.models.entities;

import java.util.Date;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "validation")
public class Validation {
	@Id
	@Column(name = "id_validation")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID code;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_ticket", nullable = true)
	private Ticket ticket;
	
	@Column(name = "expiration_date")
	private Date expirationDate;
	
	@Column(name = "validation_date")
	private Date validationDate;
	
	@Column(name = "purchase_date")
	private Boolean status;

	public Validation(Ticket ticket, Date expirationDate, Date validationDate, Boolean status) {
		super();
		this.ticket = ticket;
		this.expirationDate = expirationDate;
		this.validationDate = validationDate;
		this.status = status;
	}
	
	
}
