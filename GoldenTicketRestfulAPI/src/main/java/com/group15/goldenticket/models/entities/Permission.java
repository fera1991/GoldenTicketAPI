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
@ToString(exclude = "userXpermission")
@NoArgsConstructor
@Entity
@Table(name = "permission")
public class Permission {
	@Id
	@Column(name = "id_permission")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID code;
	
	@Column(name = "permission")
	private String permission;
	
	@OneToMany(mappedBy = "permission", fetch = FetchType.LAZY)
	@JsonIgnore
	private List<UserXPermission> userXpermission;

	public Permission(String permission) {
		super();
		this.permission = permission;
	}
	
	
}
