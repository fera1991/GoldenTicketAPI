package com.group15.goldenticket.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.group15.goldenticket.models.dtos.MessageDTO;
import com.group15.goldenticket.models.dtos.SaveCategoryDTO;
import com.group15.goldenticket.models.entities.Category;
import com.group15.goldenticket.models.entities.Event;
import com.group15.goldenticket.services.CategoryService;
import com.group15.goldenticket.utils.RequestErrorHandler;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/category")
@CrossOrigin("*")
public class CategoryController {
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private RequestErrorHandler errorHandler;
	
	@GetMapping("/all")
	public ResponseEntity<?> findAllCategory(){
		List<Category> categories = categoryService.findAll();
		
		return new ResponseEntity<>(categories,HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> findOneCategory(@PathVariable(name = "id") String code) {
		Category category = categoryService.findOneById(code);
		if(category == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(category,HttpStatus.OK);
	}
	
	@GetMapping("/event/{category}")
	public ResponseEntity<?> findAllCategoryEvent(@PathVariable(name = "category") String code) {
		Category category = categoryService.findOneById(code);
		if(category == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		List<Event> eventsByCategory = category.getEvents();
		return new ResponseEntity<>(eventsByCategory,HttpStatus.OK);
	}
	
	@PostMapping("/")
	public ResponseEntity<?> saveCategory(@RequestBody @Valid SaveCategoryDTO info, BindingResult validations){
		if(validations.hasErrors()) {
			return new ResponseEntity<>(
					errorHandler.mapErrors(validations.getFieldErrors()), 
					HttpStatus.BAD_REQUEST);
		}
		try {
			categoryService.save(info);
			return new ResponseEntity<>(
					new MessageDTO("Category Created"), HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new MessageDTO("Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteCategory(@PathVariable(name = "id") String code){
		Category category = categoryService.findOneById(code);
		if(category == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		try {
			categoryService.deleteById(code);
			return new ResponseEntity<>(
					new MessageDTO("Category Deleted"), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(
					new MessageDTO("Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
