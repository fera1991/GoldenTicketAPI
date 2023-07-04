package com.group15.goldenticket.services.implementations;


import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.group15.goldenticket.models.dtos.SaveValidationDTO;
import com.group15.goldenticket.models.entities.Ticket;
import com.group15.goldenticket.models.entities.Validation;
import com.group15.goldenticket.repositories.ValidationRepository;
import com.group15.goldenticket.services.ValidationService;

import jakarta.transaction.Transactional;

@Service
public class ValidationServiceImpl implements ValidationService{
	@Autowired
	ValidationRepository validationRepository;

	@Override
	@Transactional(rollbackOn = Exception.class)
	public void deleteById(String id) throws Exception {

	}

	@Override
	public Validation findOneById(String id) {
		try {
			UUID code = UUID.fromString(id);
			return validationRepository.findById(code).orElse(null);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public List<Validation> findAll() {
		return validationRepository.findAll();
	}

	@Override
	@Transactional(rollbackOn = Exception.class)
	public Validation save(Ticket ticket) throws Exception {
	    Date now = new Date();
	    long tenMinutesInMillis = 10 * 60 * 1000; // 10 minutos en milisegundos
	    Date newTime = new Date(now.getTime() + tenMinutesInMillis);
	    
		Validation validation = new Validation(
				ticket,
				newTime,
				false
				);
		
		return validationRepository.save(validation);
	}

	@Override
	public void update(Validation validation) throws Exception {
		validationRepository.save(validation);
	}

	@Override
	public Validation findByHash(String hash) {
		return validationRepository.findByHash(hash);
	}
}
