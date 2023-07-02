package com.group15.goldenticket.services;

import java.util.List;

import org.springframework.data.domain.Page;

import com.group15.goldenticket.models.dtos.SaveEventDTO;
import com.group15.goldenticket.models.entities.Category;
import com.group15.goldenticket.models.entities.Event;


import jakarta.validation.Valid;

public interface  EventService {
	Event save(SaveEventDTO info,Category category)throws Exception;
	void deleteById(String id) throws Exception;
	Event findOneById(String id);
	List<Event> findAll();
	Page<Event> findAll(int page, int size);
	Page<Event> findAllTitle(String title,int page, int size);
	void toggleActive(Event event,String status) throws Exception;
	void soldOut(Event event,String status) throws Exception;
	void update(Event event, @Valid SaveEventDTO info, Category category) throws Exception;
}
