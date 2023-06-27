package com.group15.goldenticket.services.implementations;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.group15.goldenticket.models.dtos.SaveCategoryDTO;
import com.group15.goldenticket.models.entities.Category;
import com.group15.goldenticket.repositories.CategoryRepository;
import com.group15.goldenticket.services.CategoryService;

import jakarta.transaction.Transactional;

@Service
public class CategoryServiceImpl implements CategoryService{
	@Autowired
	CategoryRepository categoryRepository;

	@Override
	@Transactional(rollbackOn = Exception.class)
	public void save(SaveCategoryDTO info) throws Exception {
		Category category = new Category(
				info.getName()
				);
		categoryRepository.save(category);
	}
	
	@Override
	@Transactional(rollbackOn = Exception.class)
	public void deleteById(String id) throws Exception {
		UUID code = UUID.fromString(id);
		categoryRepository.deleteById(code);
		
	}

	@Override
	public Category findOneById(String id) {
		try {
			UUID code = UUID.fromString(id);
			return categoryRepository.findById(code).orElse(null);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public List<Category> findAll() {
		return categoryRepository.findAll();
	}
}
