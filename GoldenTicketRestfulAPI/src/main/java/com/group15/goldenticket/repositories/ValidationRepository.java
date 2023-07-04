package com.group15.goldenticket.repositories;

import java.util.UUID;

import org.springframework.data.repository.ListCrudRepository;

import com.group15.goldenticket.models.entities.Validation;

public interface  ValidationRepository extends ListCrudRepository<Validation,UUID>{
	public Validation findByHash(String hash);
}
