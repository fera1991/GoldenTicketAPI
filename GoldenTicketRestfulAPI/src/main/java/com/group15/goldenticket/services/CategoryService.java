package com.group15.goldenticket.services;

import java.util.List;

import com.group15.goldenticket.models.dtos.SaveCategoryDTO;
import com.group15.goldenticket.models.entities.Category;

public interface  CategoryService {
	void save(SaveCategoryDTO info)throws Exception;
	void deleteById(String id) throws Exception;
	Category findOneById(String id);
	List<Category> findAll();
}
