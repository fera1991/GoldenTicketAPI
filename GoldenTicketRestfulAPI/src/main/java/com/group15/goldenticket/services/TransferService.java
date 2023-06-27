package com.group15.goldenticket.services;

import java.util.List;


import com.group15.goldenticket.models.entities.Transfer;

public interface  TransferService {
	//void save()throws Exception;
	void deleteById(String id) throws Exception;
	Transfer findOneById(String id);
	List<Transfer> findAll();
}
