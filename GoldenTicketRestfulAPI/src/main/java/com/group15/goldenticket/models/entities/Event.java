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
@ToString(exclude = "localities")
@NoArgsConstructor
@Entity
@Table(name = "event")
public class Event {
	@Id
	@Column(name = "id_event")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID code;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_category", nullable = true)
	private Category category;
	
	@JoinColumn(name = "place")
	private String place;
	
	@Column(name = "title")
	private String title;
	
	@Column(name = "date")
	private Date date;
	
	@Column(name = "capacity")
	private Integer capacity;
	
	@Column(name = "involved")
	private String involved;
	
	@Column(name = "image")
	private String image;
	
	@Column(name = "duration")
	private Integer duration;
	
	@Column(name = "sponsors")
	private String sponsors;
	
	@Column(name = "status")
	private String status;
	
	@OneToMany(mappedBy = "event", fetch = FetchType.LAZY)
	@JsonIgnore
	private List<Locality> localities;

	public Event(Category category, String place, String tittle, Date date, Integer capacity,
			String involved, String image, Integer duration, String sponsors, String status) {
		super();
		this.category = category;
		this.place = place;
		this.title = tittle;
		this.date = date;
		this.capacity = capacity;
		this.involved = involved;
		this.image = image;
		this.duration = duration;
		this.sponsors = sponsors;
		this.status = status;
	}

}
