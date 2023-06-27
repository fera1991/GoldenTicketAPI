package com.group15.goldenticket.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.group15.goldenticket.models.dtos.SaveTransferDTO;
import com.group15.goldenticket.models.dtos.SaveValidationDTO;
import com.group15.goldenticket.services.TicketService;
import com.group15.goldenticket.services.TransferService;
import com.group15.goldenticket.services.ValidationService;
import com.group15.goldenticket.utils.RequestErrorHandler;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/validation")
public class ValidationController {
	@Autowired
	private ValidationService validationService;
	
	@Autowired
	private TicketService ticketService;
	
	@Autowired
	private RequestErrorHandler errorHandler;
	
	@PostMapping("/")
	public ResponseEntity<?> saveValidation(@ModelAttribute @Valid SaveValidationDTO info, BindingResult validations){
		//TODO:
		return null;
	}
	
	@PatchMapping("/{id}")
	public ResponseEntity<?> CompleteValidation(@PathVariable(name = "id") String code) {
		//TODO:
		return null;
	}
	

}
