package com.group15.goldenticket.services;

import java.util.List;

import com.group15.goldenticket.models.dtos.SaveTicketDTO;
import com.group15.goldenticket.models.entities.Invoice;
import com.group15.goldenticket.models.entities.Locality;
import com.group15.goldenticket.models.entities.Ticket;
import com.group15.goldenticket.models.entities.User;

public interface  TicketService {
	void save(SaveTicketDTO info,User user,Locality locality, Invoice invoice)throws Exception;
	void deleteById(String id) throws Exception;
	Ticket findOneById(String id);
	List<Ticket> findAll();
}
