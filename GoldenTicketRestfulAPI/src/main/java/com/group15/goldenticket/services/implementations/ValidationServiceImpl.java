package com.group15.goldenticket.services.implementations;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.group15.goldenticket.models.entities.Validation;
import com.group15.goldenticket.repositories.ValidationRepository;
import com.group15.goldenticket.services.ValidationService;

@Service
public class ValidationServiceImpl implements ValidationService{
	@Autowired
	ValidationRepository validationRepository;

	@Override
	public void deleteById(String id) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Validation findOneById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Validation> findAll() {
		// TODO Auto-generated method stub
		return null;
	}
}
