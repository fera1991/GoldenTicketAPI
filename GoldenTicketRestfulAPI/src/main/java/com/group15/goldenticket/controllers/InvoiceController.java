package com.group15.goldenticket.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.group15.goldenticket.models.dtos.MessageDTO;
import com.group15.goldenticket.models.dtos.SaveInvoiceDTO;
import com.group15.goldenticket.models.entities.Invoice;
import com.group15.goldenticket.models.entities.Ticket;
import com.group15.goldenticket.models.entities.User;
import com.group15.goldenticket.services.InvoiceService;
import com.group15.goldenticket.services.TicketService;
import com.group15.goldenticket.services.UserService;
import com.group15.goldenticket.utils.JWTTools;
import com.group15.goldenticket.utils.RequestErrorHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/invoice")
@CrossOrigin("*")
public class InvoiceController {
	@Autowired
	private InvoiceService invoiceService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private TicketService ticketService;
	
	@Autowired
	private JWTTools jwtTools;
	
	
	@Autowired
	private RequestErrorHandler errorHandler;
	
	@GetMapping("/all")
	public ResponseEntity<?> findAllInvoices(){
		List<Invoice> invoices = invoiceService.findAll();		
		return new ResponseEntity<>(invoices,HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> findOneInvoice(@PathVariable(name = "id") String code) {
		Invoice invoice = invoiceService.findOneById(code);
		return new ResponseEntity<>(invoice,HttpStatus.OK);
	}
	
	@PostMapping("/")
	public ResponseEntity<?> saveInvoice(@ModelAttribute @Valid SaveInvoiceDTO info, BindingResult validations,HttpServletRequest request){
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
		try {
			Invoice newInvoice = invoiceService.save(info,user);
			
			return new ResponseEntity<>(newInvoice.getCode(), HttpStatus.CREATED);
			
		} catch (Exception e) {
			return new ResponseEntity<>(
					new MessageDTO("Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteInvoice(@PathVariable(name = "id") String code){
		Invoice invoice = invoiceService.findOneById(code);
		if(invoice == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		try {
			invoiceService.deleteById(code);
			return new ResponseEntity<>(
					new MessageDTO("Invoice Deleted"), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(
					new MessageDTO("Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
