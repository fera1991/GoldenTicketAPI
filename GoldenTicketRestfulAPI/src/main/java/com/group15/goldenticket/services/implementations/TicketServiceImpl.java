package com.group15.goldenticket.services.implementations;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.group15.goldenticket.models.dtos.SaveTicketDTO;
import com.group15.goldenticket.models.entities.Invoice;
import com.group15.goldenticket.models.entities.Locality;
import com.group15.goldenticket.models.entities.Ticket;
import com.group15.goldenticket.models.entities.User;
import com.group15.goldenticket.repositories.TicketRepository;
import com.group15.goldenticket.services.TicketService;

import jakarta.transaction.Transactional;

@Service
public class TicketServiceImpl implements TicketService{
	@Autowired
	TicketRepository ticketRepository;
	
	@Override
	@Transactional(rollbackOn = Exception.class)
	public void save(SaveTicketDTO info, User user, Locality locality, Invoice invoice) throws Exception {
		Date dateTime = new Date();
		Ticket ticket = new Ticket(
				user,
				locality,
				dateTime,
				invoice
				);
		ticketRepository.save(ticket);
	}
	
	@Override
	@Transactional(rollbackOn = Exception.class)
	public void deleteById(String id) throws Exception {
		UUID code = UUID.fromString(id);
		ticketRepository.deleteById(code);
		
	}

	@Override
	public Ticket findOneById(String id) {
		try {
			UUID code = UUID.fromString(id);
			return ticketRepository.findById(code).orElse(null);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public List<Ticket> findAll() {
		return ticketRepository.findAll();
	}

	
}
