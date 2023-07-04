package com.group15.goldenticket.controllers;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.group15.goldenticket.models.dtos.MessageDTO;
import com.group15.goldenticket.models.dtos.SaveTransferDTO;
import com.group15.goldenticket.models.dtos.SaveValidationDTO;
import com.group15.goldenticket.models.entities.Ticket;
import com.group15.goldenticket.models.entities.Validation;
import com.group15.goldenticket.services.TicketService;
import com.group15.goldenticket.services.TransferService;
import com.group15.goldenticket.services.ValidationService;
import com.group15.goldenticket.utils.RequestErrorHandler;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/validation")
@CrossOrigin("*")
public class ValidationController {
	@Autowired
	private ValidationService validationService;
	
	@Autowired
	private TicketService ticketService;
	
	@Autowired
	private RequestErrorHandler errorHandler;
	
	@PostMapping("/{id}")
	public ResponseEntity<?> saveValidation(@PathVariable(name = "id") String code){
		Ticket ticket = ticketService.findOneById(code);
		if(ticket == null) {
			return new ResponseEntity<>(new MessageDTO("Ticket Not Found"),HttpStatus.NOT_FOUND);
		}
		
		
		List<Validation> validations = ticket.getValidations();
	    
	    // Verificar si ya existe una validación con estado verdadero
	    for (Validation validation : validations) {
	        if (validation.getStatus()) {
	            return new ResponseEntity<>(new MessageDTO("Validation Already Completed"), HttpStatus.OK);
	        }
	    }
		
		
		try {
			Validation validation = validationService.save(ticket);
			String validationCode = validation.getCode().toString();
			
			 // Generar el hash del código de validación
	        MessageDigest digest = MessageDigest.getInstance("SHA-256");
	        byte[] hashBytes = digest.digest(validationCode.getBytes(StandardCharsets.UTF_8));
	        String hash = bytesToHex(hashBytes);
	        
	        validation.setHash(hash);
	        validationService.update(validation);
			
			return new ResponseEntity<>(hash, HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new MessageDTO("Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	private static String bytesToHex(byte[] bytes) {
	    StringBuilder result = new StringBuilder();
	    for (byte b : bytes) {
	        result.append(String.format("%02x", b));
	    }
	    return result.toString();
	}
	
	@PatchMapping("/{hash}")
	public ResponseEntity<?> CompleteValidation(@PathVariable(name = "hash") String hash) {
		Validation validation = validationService.findByHash(hash);
		if(validation == null) {
			return new ResponseEntity<>(new MessageDTO("Validation Not Found"),HttpStatus.NOT_FOUND);
		}
		Date now = new Date();
		
		System.out.println("Fecha y hora actual: " + now);
	    System.out.println("Fecha y hora de expiración: " + validation.getExpirationDate());
	    
	    if (validation.getStatus()) {
	        return new ResponseEntity<>(new MessageDTO("Validation Already Completed"), HttpStatus.OK);
	    }
		
		if (now.before(validation.getExpirationDate())) {

	        try {
	        	validation.setStatus(true); 
	        	validation.setValidationDate(now);
		        validationService.update(validation); 
		        
				return new ResponseEntity<>(
						new MessageDTO("Validation Compleated"), HttpStatus.CREATED);
			} catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<>(
						new MessageDTO("Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
			}
	    } else {
	        return new ResponseEntity<>(new MessageDTO("Validation Expired"), HttpStatus.OK);
	    }

	}
	

}
