package com.group15.goldenticket.services.implementations;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.group15.goldenticket.models.entities.Transfer;
import com.group15.goldenticket.repositories.TransferRepository;
import com.group15.goldenticket.services.TransferService;

@Service
public class TranseferServiceImpl implements TransferService{
	@Autowired
	TransferRepository transferRepository;

	@Override
	public void deleteById(String id) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Transfer findOneById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Transfer> findAll() {
		// TODO Auto-generated method stub
		return null;
	}
}
