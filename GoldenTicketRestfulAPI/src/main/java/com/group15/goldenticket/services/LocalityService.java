package com.group15.goldenticket.services;

import java.util.List;

import com.group15.goldenticket.models.dtos.SaveLocalityDTO;
import com.group15.goldenticket.models.entities.Event;
import com.group15.goldenticket.models.entities.Locality;

public interface  LocalityService {
	void save(SaveLocalityDTO info,Event event)throws Exception;
	void deleteById(String id) throws Exception;
	Locality findOneById(String id);
	List<Locality> findAll();
}
