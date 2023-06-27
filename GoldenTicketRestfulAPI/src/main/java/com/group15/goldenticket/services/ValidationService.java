package com.group15.goldenticket.services;

import java.util.List;


import com.group15.goldenticket.models.entities.Validation;

public interface  ValidationService {
	//void save()throws Exception;
	void deleteById(String id) throws Exception;
	Validation findOneById(String id);
	List<Validation> findAll();
}
