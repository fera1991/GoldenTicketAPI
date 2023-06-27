package com.group15.goldenticket.services.implementations;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.group15.goldenticket.models.dtos.SaveEventDTO;
import com.group15.goldenticket.models.entities.Category;
import com.group15.goldenticket.models.entities.Event;
import com.group15.goldenticket.repositories.EventRepository;
import com.group15.goldenticket.services.EventService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
public class EventServiceImpl implements EventService{
	@Autowired
	EventRepository eventRepository;
	
	@Override
	@Transactional(rollbackOn = Exception.class)
	public void save(SaveEventDTO info,Category category) throws Exception {
		String format = "dd/MM/yyyy";
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		Date date = dateFormat.parse(info.getDate());
		
		Event event = new Event(
				category,
				info.getPlace(),
				info.getTittle(),
				date,
				info.getCapacity(),
				info.getInvolved(),
				info.getImage(),
				info.getDuration(),
				info.getSponsors(),
				"Active"
				);
		eventRepository.save(event);
	}
	
	@Override
	@Transactional(rollbackOn = Exception.class)
	public void update(Event event, @Valid SaveEventDTO info, Category category) throws Exception {
		String format = "yyyy-MM-dd";
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		Date date = dateFormat.parse(info.getDate());
		
		Event updateEvent = event;
		updateEvent.setCategory(category);
		updateEvent.setPlace(info.getPlace());
		updateEvent.setTitle(info.getTittle());
		updateEvent.setDate(date);
		updateEvent.setCapacity(info.getCapacity());
		updateEvent.setInvolved(info.getInvolved());
		updateEvent.setImage(info.getImage());
		updateEvent.setDuration(info.getDuration());
		updateEvent.setSponsors(info.getSponsors());;
		eventRepository.save(updateEvent);
	}
	
	@Override
	@Transactional(rollbackOn = Exception.class)
	public void deleteById(String id) throws Exception {
		UUID code = UUID.fromString(id);
		eventRepository.deleteById(code);
	}

	@Override
	public Event findOneById(String id) {
		try {
			UUID code = UUID.fromString(id);
			return eventRepository.findById(code).orElse(null);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Page<Event> findAll(int page, int size) {
		Pageable pageable = PageRequest.of(page,size);
		return eventRepository.findAll(pageable);
	}

	@Override
	@Transactional(rollbackOn = Exception.class)
	public void toggleActive(Event event,String status) throws Exception {
		Event updateEvent = event;
		event.setStatus(status);
		eventRepository.save(updateEvent);
	}

	@Override
	@Transactional(rollbackOn = Exception.class)
	public void soldOut(Event event,String status) throws Exception {
		Event updateEvent = event;
		event.setStatus(status);
		eventRepository.save(updateEvent);
	}

	@Override
	public Page<Event> findAllTitle(String title, int page, int size) {
		Pageable pageable = PageRequest.of(page,size);
		return eventRepository.findByTitleContaining(title,pageable);
	}

	@Override
	public List<Event> findAll() {
		return eventRepository.findAll();
	}

	
	



	
}
