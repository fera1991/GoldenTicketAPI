package com.group15.goldenticket.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.group15.goldenticket.models.dtos.MessageDTO;
import com.group15.goldenticket.models.dtos.SavePermissionDTO;
import com.group15.goldenticket.models.entities.Permission;
import com.group15.goldenticket.services.PermissionService;
import com.group15.goldenticket.utils.RequestErrorHandler;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/permission")
public class PermissionController {
	
	@Autowired
	private PermissionService permissionService;
	
	@Autowired
	private RequestErrorHandler errorHandler;
	
	@GetMapping("/{id}")
	public ResponseEntity<?> findOnePermission(@PathVariable(name = "id") String code) {
		Permission permission = permissionService.findOneById(code);
		if(permission == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(permission,HttpStatus.OK);
	}
	
	@PostMapping("/")
	public ResponseEntity<?> savePermission(@RequestBody @Valid SavePermissionDTO info, BindingResult validations){
		if(validations.hasErrors()) {
			return new ResponseEntity<>(
					errorHandler.mapErrors(validations.getFieldErrors()), 
					HttpStatus.BAD_REQUEST);
		}
		try {
			permissionService.save(info);
			return new ResponseEntity<>(
					new MessageDTO("Permission Created"), HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new MessageDTO("Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/all")
	public ResponseEntity<?> findAllCategory() {
		List<Permission> permissions = permissionService.findAll();
		return new ResponseEntity<>(permissions,HttpStatus.OK);
	}
}
