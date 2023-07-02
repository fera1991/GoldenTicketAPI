package com.group15.goldenticket.services.implementations;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.group15.goldenticket.models.dtos.SaveCategoryDTO;
import com.group15.goldenticket.models.entities.Category;
import com.group15.goldenticket.models.entities.Event;
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

	@Override
	public Page<Event> getPaginatedList(List<Event> list, int page, int size) {
		int startIndex = page * size;
        int endIndex = Math.min(startIndex + size, list.size());
        
        List<Event> sublist = list.subList(startIndex, endIndex);
        PageRequest pageRequest = PageRequest.of(page,size);
        
        return new PageImpl<>(sublist,pageRequest,list.size());
	}
}
