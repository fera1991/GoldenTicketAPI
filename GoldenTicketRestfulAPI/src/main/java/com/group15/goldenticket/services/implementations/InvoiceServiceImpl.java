package com.group15.goldenticket.services.implementations;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.group15.goldenticket.models.dtos.SaveInvoiceDTO;
import com.group15.goldenticket.models.entities.Invoice;
import com.group15.goldenticket.models.entities.Ticket;
import com.group15.goldenticket.models.entities.User;
import com.group15.goldenticket.repositories.InvoiceRepository;
import com.group15.goldenticket.services.InvoiceService;

@Service
public class InvoiceServiceImpl implements InvoiceService{
	@Autowired
	InvoiceRepository invoiceRepository;

	@Override
	public void save(SaveInvoiceDTO info, User user, Ticket ticket) throws Exception {
		Date dateTime = new Date();
		Invoice invoice = new Invoice(
				dateTime,
				user,
				ticket,
				info.getTotal()
				);
		
	}
	
	@Override
	public void deleteById(String id) throws Exception {
		UUID code = UUID.fromString(id);
		invoiceRepository.deleteById(code);
		
	}

	@Override
	public Invoice findOneById(String id) {
		try {
			UUID code = UUID.fromString(id);
			return invoiceRepository.findById(code).orElse(null);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public List<Invoice> findAll() {
		return invoiceRepository.findAll();
	}

}
