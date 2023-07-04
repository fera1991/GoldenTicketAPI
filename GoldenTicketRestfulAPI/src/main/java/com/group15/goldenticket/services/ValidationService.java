package com.group15.goldenticket.services;

import java.util.List;

import com.group15.goldenticket.models.dtos.SaveValidationDTO;
import com.group15.goldenticket.models.entities.Ticket;
import com.group15.goldenticket.models.entities.Validation;

public interface  ValidationService {
	Validation save(Ticket ticket)throws Exception;
	void deleteById(String id) throws Exception;
	void update(Validation validation) throws Exception;
	Validation findOneById(String id);
	Validation findByHash(String hash);
	List<Validation> findAll();
}
