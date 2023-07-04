package com.group15.goldenticket.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.group15.goldenticket.models.dtos.GoogleDTO;
import com.group15.goldenticket.models.dtos.LoginDTO;
import com.group15.goldenticket.models.dtos.MessageDTO;
import com.group15.goldenticket.models.dtos.RegisterDTO;
import com.group15.goldenticket.models.dtos.TokenDTO;
import com.group15.goldenticket.models.entities.Permission;
import com.group15.goldenticket.models.entities.Token;
import com.group15.goldenticket.models.entities.User;
import com.group15.goldenticket.services.PermissionService;
import com.group15.goldenticket.services.UserService;
import com.group15.goldenticket.services.UserXPermissionService;
import com.group15.goldenticket.utils.RequestErrorHandler;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthController {
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserXPermissionService userXpermissionService;
	
	@Autowired
	private PermissionService permissionService;
	
	@Autowired
	private RequestErrorHandler errorHandler;
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @ModelAttribute LoginDTO info, BindingResult validations) {
		System.out.println(info);
		if(validations.hasErrors()) {
			return new ResponseEntity<>(
					errorHandler.mapErrors(validations.getFieldErrors()), 
					HttpStatus.BAD_REQUEST);
		}
		
		User user = userService.findOneByIdentifier(info.getIdentifier());
		
		if(user == null) {
			return new ResponseEntity<>(new MessageDTO("User Not Found"),HttpStatus.NOT_FOUND);
		}
		
		if(!userService.comparePassword(info.getPassword(), user.getPassword())) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		try {
			Token token = userService.registerToken(user);
			return new ResponseEntity<>(new TokenDTO(token), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/google")
	public ResponseEntity<?> loginWithGoogle(@Valid @ModelAttribute GoogleDTO info, BindingResult validations) {
		System.out.println(info);
		if(validations.hasErrors()) {
			return new ResponseEntity<>(
					errorHandler.mapErrors(validations.getFieldErrors()), 
					HttpStatus.BAD_REQUEST);
		}
		
		User user = userService.findOneByIdentifier(info.getEmail());
		
		if(user == null) {
			return new ResponseEntity<>(new MessageDTO("User Not Found"),HttpStatus.NOT_FOUND);
		}
		try {
			Token token = userService.registerToken(user);
			return new ResponseEntity<>(new TokenDTO(token), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/register")
	public ResponseEntity<?> register(@ModelAttribute @Valid RegisterDTO info, BindingResult validations){
		if(validations.hasErrors()) {
			return new ResponseEntity<>(
					errorHandler.mapErrors(validations.getFieldErrors()), 
					HttpStatus.BAD_REQUEST);
		}
		List<User> response = userService.findAll();
		
		boolean flag = false;
		for (int i = 0; i< response.size(); i++) {
			if(response.get(i).getEmail().equals(info.getEmail())) {
				flag = true;
			}
		}
		if (flag) {
	        return new ResponseEntity<>(
	                new MessageDTO("Email already exists"), HttpStatus.ACCEPTED);
	    }
		Permission permission = permissionService.findOneById("ee08b865-c0d6-4d9a-b41d-49c3771cff0c");
		if(permission == null) {
			return new ResponseEntity<>(new MessageDTO("Permission Not Found"),HttpStatus.NOT_FOUND);
		}
		try {
			User newUser = userService.register(info);
			userXpermissionService.save(newUser,permission);
			
			return new ResponseEntity<>(
					new MessageDTO("User Created"), HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new MessageDTO("Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
