package com.group15.goldenticket.models.entities;

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
@ToString(exclude = "tickets")
@NoArgsConstructor
@Entity
@Table(name = "locality")
public class Locality {
	
	@Id
	@Column(name = "id_locality")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID code;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_event", nullable = true)
	private Event event;
	
	@Column(name = "locality_name")
	private String name;
	
	@Column(name = "price")
	private Float price;
	
	@Column(name = "available_quantity")
	private Integer availableQuantity;
	
	
	@OneToMany(mappedBy = "locality", fetch = FetchType.LAZY)
	@JsonIgnore
	private List<Ticket> tickets;
	
	public Locality(Event event, String name, Float price, Integer availableQuantity) {
		super();
		this.event = event;
		this.name = name;
		this.price = price;
		this.availableQuantity = availableQuantity;
	}
}
