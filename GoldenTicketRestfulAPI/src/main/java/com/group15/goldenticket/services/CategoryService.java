package com.group15.goldenticket.services;

import java.util.List;

import org.springframework.data.domain.Page;

import com.group15.goldenticket.models.dtos.SaveCategoryDTO;
import com.group15.goldenticket.models.entities.Category;
import com.group15.goldenticket.models.entities.Event;

public interface  CategoryService {
	void save(SaveCategoryDTO info)throws Exception;
	void deleteById(String id) throws Exception;
	Category findOneById(String id);
	List<Category> findAll();
	Page<Event> getPaginatedList(List<Event> list, int page, int size);
}
