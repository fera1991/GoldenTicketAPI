package com.group15.goldenticket.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.group15.goldenticket.models.dtos.MessageDTO;
import com.group15.goldenticket.models.dtos.SaveTicketDTO;
import com.group15.goldenticket.models.entities.Invoice;
import com.group15.goldenticket.models.entities.Locality;
import com.group15.goldenticket.models.entities.Ticket;
import com.group15.goldenticket.models.entities.User;
import com.group15.goldenticket.services.InvoiceService;
import com.group15.goldenticket.services.LocalityService;
import com.group15.goldenticket.services.TicketService;
import com.group15.goldenticket.services.UserService;
import com.group15.goldenticket.utils.JWTTools;
import com.group15.goldenticket.utils.RequestErrorHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/ticket")
@CrossOrigin("*")
public class TicketController {
	@Autowired
	private TicketService ticketService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private LocalityService localityService;
	
	@Autowired
	private InvoiceService invoiceService;
	
	@Autowired
	private JWTTools jwtTools;
	
	@Autowired
	private RequestErrorHandler errorHandler;
	
	@GetMapping("/all")
	public ResponseEntity<?> findAllTicket(){
		List<Ticket> tickets = ticketService.findAll();
		return new ResponseEntity<>(tickets,HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> findOneTicket(@PathVariable(name = "id") String code) {
		Ticket ticket = ticketService.findOneById(code);
		if(ticket == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(ticket,HttpStatus.OK);
	}
	
	@PostMapping("/")
	public ResponseEntity<?> saveTicket(@ModelAttribute @Valid SaveTicketDTO info, BindingResult validations,HttpServletRequest request){
		if(validations.hasErrors()) {
			return new ResponseEntity<>(
					errorHandler.mapErrors(validations.getFieldErrors()), 
					HttpStatus.BAD_REQUEST);
		}
		String tokenHeader = request.getHeader("Authorization");
    	String token = tokenHeader.substring(7);
    	
    	
		User user = userService.findOneByIdentifier(jwtTools.getUsernameFrom(token));
		if(user == null) {
			return new ResponseEntity<>(new MessageDTO("User Not Found"),HttpStatus.NOT_FOUND);
		}
		
		Locality locality = localityService.findOneById(info.getLocalityId());
		if(locality == null) {
			return new ResponseEntity<>(new MessageDTO("Locality Not Found"),HttpStatus.NOT_FOUND);
		}
		
		Invoice invoice = invoiceService.findOneById(info.getInvoiceId());
		if(invoice == null) {
			return new ResponseEntity<>(new MessageDTO("Invoice Not Found"),HttpStatus.NOT_FOUND);
		}
		
		if(locality.getTickets().size() == locality.getAvailableQuantity()) {
			return new ResponseEntity<>(new MessageDTO("Locality is Sold-out"),HttpStatus.NOT_FOUND);
		}
		
		
		try {
			ticketService.save(info,user,locality,invoice);
			return new ResponseEntity<>(
					new MessageDTO("Ticket Created"), HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new MessageDTO("Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
