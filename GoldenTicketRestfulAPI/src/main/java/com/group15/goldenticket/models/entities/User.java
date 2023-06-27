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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString(exclude = {"tickets","userXpermission","transfersSent","receivedTransfers","invoices"})
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User {
	@Id
	@Column(name = "id_user")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID code;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "username")
	private String username;
	
	@Column(name = "email")
	private String email;
	
	
	@JsonIgnore
	@Column(name = "password")
	private String password;
	
	@Column(name = "status")
	private Boolean status;
	
	
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	@JsonIgnore
	private List<Ticket> tickets;
	
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	@JsonIgnore
	private List<UserXPermission> userXpermission;
	
	@OneToMany(mappedBy = "userSend", fetch = FetchType.LAZY)
	@JsonIgnore
	private List<Transfer> transfersSent;
	
	@OneToMany(mappedBy = "userReceive", fetch = FetchType.LAZY)
	@JsonIgnore
	private List<Transfer> receivedTransfers;
	
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	@JsonIgnore
	private List<Invoice> invoices;

	public User(String name, String username, String email, String password, Boolean status) {
		super();
		this.name = name;
		this.username = username;
		this.email = email;
		this.password = password;
		this.status = status;
	}
	
	
	
}
