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
@NoArgsConstructor
@Entity
@Table(name = "invoice")
@ToString(exclude = {"tickets"})
public class Invoice {
	@Id
	@Column(name = "id_invoice")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID code;
	
	@Column(name = "date_issue")
	private Date dateIssue;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_user", nullable = true)
	private User user;
	
	@Column(name = "total")
	private Float total;
	
	@OneToMany(mappedBy = "invoice", fetch = FetchType.LAZY)
	@JsonIgnore
	private List<Ticket> tickets;

	public Invoice(Date dateIssue, User user, Ticket ticket, Float total) {
		super();
		this.dateIssue = dateIssue;
		this.user = user;
		this.total = total;
	}
	
	
}
