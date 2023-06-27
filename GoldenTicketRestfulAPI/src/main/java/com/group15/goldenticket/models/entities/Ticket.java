package com.group15.goldenticket.models.entities;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString(exclude = {"validations","transfers","Invoices"})
@NoArgsConstructor
@Entity
@Table(name = "ticket")
public class Ticket {
	@Id
	@Column(name = "id_ticket")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID code;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_user", nullable = true)
	private User user;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_locality", nullable = true)
	private Locality locality;
	
	@Column(name = "purchase_date")
	private Date purchaseDate;
	
	@OneToMany(mappedBy = "ticket", fetch = FetchType.LAZY)
	@JsonIgnore
	private List<Validation> validations;
	
	@OneToMany(mappedBy = "ticket", fetch = FetchType.LAZY)
	@JsonIgnore
	private List<Transfer> transfers;
	
	@OneToMany(mappedBy = "ticket", fetch = FetchType.LAZY)
	@JsonIgnore
	private List<Invoice> Invoices;

	public Ticket(User user, Locality locality, Date purchaseDate) {
		super();
		this.user = user;
		this.locality = locality;
		this.purchaseDate = purchaseDate;
	}
	
	
	
}
