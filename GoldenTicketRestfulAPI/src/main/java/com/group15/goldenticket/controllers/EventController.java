package com.group15.goldenticket.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.group15.goldenticket.models.dtos.MessageDTO;
import com.group15.goldenticket.models.dtos.PageDTO;
import com.group15.goldenticket.models.dtos.SaveEventDTO;
import com.group15.goldenticket.models.entities.Category;
import com.group15.goldenticket.models.entities.Event;
import com.group15.goldenticket.models.entities.Locality;
import com.group15.goldenticket.services.CategoryService;
import com.group15.goldenticket.services.EventService;
import com.group15.goldenticket.utils.RequestErrorHandler;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/event")
@CrossOrigin("*")
public class EventController {
	
	@Autowired
	private EventService eventService;
	
	@Autowired
	private CategoryService categoryService;

	
	@Autowired
	private RequestErrorHandler errorHandler;
	
	@GetMapping("/ticket/{id}")
	public ResponseEntity<?> findAmoutOfTickets(@PathVariable(name = "id") String code) {
		Event event = eventService.findOneById(code);
		if(event == null) {
			return new ResponseEntity<>(new MessageDTO("Event Not Found"),HttpStatus.NOT_FOUND);
		}
		int ticketQuantity = 0;
		
		for (Locality localities : event.getLocalities()) {
			ticketQuantity =+ localities.getTickets().size();
		}
		return new ResponseEntity<>(ticketQuantity,HttpStatus.OK);
	}
	
	@GetMapping("/locality/{id}")
	public ResponseEntity<?> findAllLocalityEvent(@PathVariable(name = "id") String code) {
		Event event = eventService.findOneById(code);
		if(event == null) {
			return new ResponseEntity<>(new MessageDTO("Event Not Found"),HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(event.getLocalities(),HttpStatus.OK);
	}
	
	@GetMapping("/all")
	public ResponseEntity<?> findAllEvents(@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String title){
		if(title != null && !title.isEmpty()) {
			Page<Event> TitleEvents = eventService.findAllTitle(title,page,size);
			return new ResponseEntity<>(new PageDTO<Event>(
					TitleEvents.getContent(),
					TitleEvents.getNumber(),
					TitleEvents.getSize(),
					TitleEvents.getTotalElements(),
					TitleEvents.getTotalPages())
					,HttpStatus.OK);
		}
		
		Page<Event> events = eventService.findAll(page,size);
		return new ResponseEntity<>(new PageDTO<Event>(
				events.getContent(),
				events.getNumber(),
				events.getSize(),
				events.getTotalElements(),
				events.getTotalPages())
				,HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> findOneEvent(@PathVariable(name = "id") String code) {
		Event event = eventService.findOneById(code);
		if(event == null) {
			return new ResponseEntity<>(new MessageDTO("Event Not Found"),HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(event,HttpStatus.OK);
	}
	
	@PostMapping("/")
	public ResponseEntity<?> saveEvent(@ModelAttribute @Valid SaveEventDTO info, BindingResult validations){
		if(validations.hasErrors()) {
			return new ResponseEntity<>(
					errorHandler.mapErrors(validations.getFieldErrors()), 
					HttpStatus.BAD_REQUEST);
		}
		Category category = categoryService.findOneById(info.getCategoryId());
		if(category == null) {
			return new ResponseEntity<>(new MessageDTO("Category not Found"),HttpStatus.NOT_FOUND);
		}
		List<Event> events = eventService.findAll();
		Boolean flag = false;
		
		for (Event event : events) {
			if(info.getTittle().contentEquals(event.getTitle())) {
				flag = true;
			}
			
		}
		if(flag) {
			return new ResponseEntity<>(new MessageDTO("Event Duplicated"),HttpStatus.CONFLICT);
		}
		
		
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			format.setTimeZone(TimeZone.getTimeZone("GMT"));
	        Date date = format.parse(info.getDate());
			Event newEvent = eventService.save(info,category,date);
			return new ResponseEntity<>(newEvent.getCode(), HttpStatus.CREATED);
			
		} catch (Exception e) {
			 if (e instanceof ParseException) {
			        return new ResponseEntity<>(new MessageDTO("Invalid date format"), HttpStatus.BAD_REQUEST);
			    } else {
			        e.printStackTrace();
			        return new ResponseEntity<>(new MessageDTO("Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
			    }
		}
	}
	
	@PatchMapping("/toggle-active/{id}")
	public ResponseEntity<?> toggleActive(@PathVariable(name = "id") String code) {
		Event event = eventService.findOneById(code);
		if(event == null) {
			return new ResponseEntity<>(new MessageDTO("Event Not Found"),HttpStatus.NOT_FOUND);
		}
		try {
			if(event.getStatus().equals("Active")) {
				eventService.toggleActive(event,"Inactive");
			}else {
				eventService.toggleActive(event,"Active");
			}
			return new ResponseEntity<>(
					new MessageDTO("Event Updated"), HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new MessageDTO("Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@PatchMapping("/sold-out/{id}")
	public ResponseEntity<?> soldOut(@ModelAttribute(name = "id") String code) {
		Event event = eventService.findOneById(code);
		if(event == null) {
			return new ResponseEntity<>(new MessageDTO("Event Not Found"),HttpStatus.NOT_FOUND);
		}
		try {
			if(event.getStatus().equals("SoldOut")) {
				eventService.toggleActive(event,"Active");
			}else {
				eventService.toggleActive(event,"SoldOut");
			}
			return new ResponseEntity<>(
					new MessageDTO("Event Updated"), HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new MessageDTO("Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	
	@PatchMapping("/{id}")
	public ResponseEntity<?> UpdateEvent(@ModelAttribute @Valid SaveEventDTO info, BindingResult validations,@PathVariable(name = "id") String code) {
		Event event = eventService.findOneById(code);
		if(event == null) {
			return new ResponseEntity<>(new MessageDTO("Event Not Found"),HttpStatus.NOT_FOUND);
		}
		if(validations.hasErrors()) {
			return new ResponseEntity<>(
					errorHandler.mapErrors(validations.getFieldErrors()), 
					HttpStatus.BAD_REQUEST);
		}
		Category category = categoryService.findOneById(info.getCategoryId());
		if(category == null) {
			return new ResponseEntity<>(new MessageDTO("Category not Found"),HttpStatus.NOT_FOUND);
		}
		
		List<Event> response = eventService.findAll();
		boolean flag = false;
		
		for (int i = 0; i< response.size(); i++) {
			if(response.get(i).getTitle().equals(info.getTittle())) {
				flag = true;
			}
		}
		
		if(info.getTittle() == event.getTitle()) {
			flag = false;
		}
		
		if (flag) {
	        return new ResponseEntity<>(
	                new MessageDTO("Event name already exists"), HttpStatus.CONFLICT);
	    }

		
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			format.setTimeZone(TimeZone.getTimeZone("GMT"));
	        Date date = format.parse(info.getDate());
			eventService.update(event,info,category, date);
			return new ResponseEntity<>(
					new MessageDTO("Event Updated"), HttpStatus.CREATED);
		} catch (Exception e) {
			 if (e instanceof ParseException) {
			        return new ResponseEntity<>(new MessageDTO("Invalid date format"), HttpStatus.BAD_REQUEST);
			    } else {
			        e.printStackTrace();
			        return new ResponseEntity<>(new MessageDTO("Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
			    }
		}
	}
	

}
