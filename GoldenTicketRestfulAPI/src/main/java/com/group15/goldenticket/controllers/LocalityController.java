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
import com.group15.goldenticket.models.dtos.SaveLocalityDTO;
import com.group15.goldenticket.models.entities.Event;
import com.group15.goldenticket.models.entities.Locality;
import com.group15.goldenticket.services.EventService;
import com.group15.goldenticket.services.LocalityService;
import com.group15.goldenticket.utils.RequestErrorHandler;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/locality")
@CrossOrigin("*")
public class LocalityController {
	@Autowired
	private LocalityService localityService;
	
	@Autowired
	private EventService eventService;
	
	@Autowired
	private RequestErrorHandler errorHandler;
	
	
	
	@GetMapping("/all")
	public ResponseEntity<?> findAllLocality(){
		List<Locality> localities = localityService.findAll();
		return new ResponseEntity<>(localities,HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> findOneLocality(@PathVariable(name = "id") String code) {
		Locality locality = localityService.findOneById(code);
		if(locality == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(locality,HttpStatus.OK);
	}
	
	@GetMapping("/tikets/{id}")
	public ResponseEntity<?> findAllTickets(@PathVariable(name = "id") String code) {
		Locality locality = localityService.findOneById(code);
		if(locality == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(locality.getTickets().size(),HttpStatus.OK);
	}
	
	@PostMapping("/save")
	public ResponseEntity<?> saveLocality(@ModelAttribute @Valid SaveLocalityDTO info, BindingResult validations){
		if(validations.hasErrors()) {
			return new ResponseEntity<>(
					errorHandler.mapErrors(validations.getFieldErrors()), 
					HttpStatus.BAD_REQUEST);
		}
		Event event = eventService.findOneById(info.getEventId());
		if(event == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		try {
			localityService.save(info, event);
			return new ResponseEntity<>(
					new MessageDTO("Locality Created"), HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(
					new MessageDTO("Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteLocality(@PathVariable(name = "id") String code){
		Locality locality = localityService.findOneById(code);
		if(locality == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		try {
			localityService.deleteById(code);
			return new ResponseEntity<>(
					new MessageDTO("Locality Deleted"), HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(
					new MessageDTO("Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
