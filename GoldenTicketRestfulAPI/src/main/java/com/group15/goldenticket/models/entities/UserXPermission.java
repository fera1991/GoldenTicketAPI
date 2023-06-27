package com.group15.goldenticket.models.entities;

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
@Table(name = "userxpermission")
public class UserXPermission {
	@Id
	@Column(name = "id_userxpermission")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID code;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_permission", nullable = true)
	private Permission permission;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_user", nullable = true)
	private User user;

	public UserXPermission(Permission permission, User user) {
		super();
		this.permission = permission;
		this.user = user;
	}
	
	
}
