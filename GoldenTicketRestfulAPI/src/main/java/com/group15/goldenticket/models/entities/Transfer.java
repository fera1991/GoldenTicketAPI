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
@Table(name = "transfer")
public class Transfer {
	@Id
	@Column(name = "id_transfer")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID code;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_sending_user", nullable = true)
	private User userSend;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_receiving_user ", nullable = true)
	private User userReceive;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_ticket", nullable = true)
	private Ticket ticket;
	
	@Column(name = "reception_date")
	private Date receptionDate;
	
	@Column(name = "issuance_date")
	private Date issuanceDate;
	
	@Column(name = "hash_email")
	private String hashEmail;
	
	@Column(name = "status")
	private Boolean status;

	public Transfer(User userSend, User userReceive, Ticket ticket, Date receptionDate, Date issuanceDate,
			String hashEmail, Boolean status) {
		super();
		this.userSend = userSend;
		this.userReceive = userReceive;
		this.ticket = ticket;
		this.receptionDate = receptionDate;
		this.issuanceDate = issuanceDate;
		this.hashEmail = hashEmail;
		this.status = status;
	}
	
	
}
