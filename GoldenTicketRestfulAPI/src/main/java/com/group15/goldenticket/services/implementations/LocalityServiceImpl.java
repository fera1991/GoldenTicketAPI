package com.group15.goldenticket.services.implementations;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.group15.goldenticket.models.dtos.SaveLocalityDTO;
import com.group15.goldenticket.models.entities.Event;
import com.group15.goldenticket.models.entities.Locality;
import com.group15.goldenticket.repositories.LocalityRepository;
import com.group15.goldenticket.services.LocalityService;

import jakarta.transaction.Transactional;

@Service
public class LocalityServiceImpl implements LocalityService{
	@Autowired
	LocalityRepository localityRepository;
	
	
	@Override
	@Transactional(rollbackOn = Exception.class)
	public void save(SaveLocalityDTO info, Event event) throws Exception {
		Locality locality = new Locality(
				event,
				info.getName(),
				info.getPrice(),
				info.getAvailableQuantity()
				);
		localityRepository.save(locality);
	}
	
	@Override
	@Transactional(rollbackOn = Exception.class)
	public void deleteById(String id) throws Exception {
		UUID code = UUID.fromString(id);
		localityRepository.deleteById(code);
	}

	@Override
	public Locality findOneById(String id) {
		try {
			UUID code = UUID.fromString(id);
			return localityRepository.findById(code).orElse(null);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public List<Locality> findAll() {
		return localityRepository.findAll();
	}


}
