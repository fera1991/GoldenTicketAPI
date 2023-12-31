package com.group15.goldenticket.services;

import java.util.List;

import com.group15.goldenticket.models.dtos.SaveInvoiceDTO;
import com.group15.goldenticket.models.entities.Invoice;
import com.group15.goldenticket.models.entities.Ticket;
import com.group15.goldenticket.models.entities.User;

public interface  InvoiceService {
	Invoice save(SaveInvoiceDTO info,User user)throws Exception;
	void deleteById(String id) throws Exception;
	Invoice findOneById(String id);
	List<Invoice> findAll();
}
