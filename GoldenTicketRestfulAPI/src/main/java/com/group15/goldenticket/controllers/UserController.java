package com.group15.goldenticket.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.group15.goldenticket.models.dtos.UserPermissionDTO;
import com.group15.goldenticket.models.entities.Permission;
import com.group15.goldenticket.models.entities.Ticket;
import com.group15.goldenticket.models.entities.User;
import com.group15.goldenticket.models.entities.UserXPermission;
import com.group15.goldenticket.models.dtos.ChangePasswordDTO;
import com.group15.goldenticket.models.dtos.MessageDTO;
import com.group15.goldenticket.models.dtos.PageDTO;
import com.group15.goldenticket.models.dtos.PermissionVerificationDTO;
import com.group15.goldenticket.models.dtos.SaveUserDTO;
import com.group15.goldenticket.models.dtos.ShowTicketDTO;
import com.group15.goldenticket.models.dtos.ShowUserDTO;
import com.group15.goldenticket.models.dtos.UpdateUserDTO;
import com.group15.goldenticket.models.dtos.UserPermissionAsignedDTO;
import com.group15.goldenticket.services.PermissionService;
import com.group15.goldenticket.services.UserService;
import com.group15.goldenticket.services.UserXPermissionService;
import com.group15.goldenticket.utils.JWTTools;
import com.group15.goldenticket.utils.RequestErrorHandler;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class UserController {
	@Autowired
	private UserService userService;
	
	@Autowired
	private JWTTools jwtTools;
	
	@Autowired
	private PermissionService permissionService;
	
	@Autowired
	private UserXPermissionService userXpermissionService;
	
	@Autowired
	private RequestErrorHandler errorHandler;
	
	@GetMapping("/info")
	public ResponseEntity<?> getUserInfo(HttpServletRequest request){
		String tokenHeader = request.getHeader("Authorization");
    	String token = tokenHeader.substring(7);
    	
    	User user = userService.findOneByIdentifier(jwtTools.getUsernameFrom(token));
    	if(user == null) {
			return new ResponseEntity<>(
					new MessageDTO("user not found"), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(new ShowUserDTO(
										user.getCode(),
										user.getName(),
										user.getUsername(),
										user.getEmail()
													),HttpStatus.OK);
	}
	
	@PostMapping("/create")
	public ResponseEntity<?> createUser(@ModelAttribute @Valid SaveUserDTO info, BindingResult validations){
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
	                new MessageDTO("Email already exists"), HttpStatus.CONFLICT);
	    }
		
		try {
			userService.save(info);
			return new ResponseEntity<>(
					new MessageDTO("User Created"), HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new MessageDTO("Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/all")
	public ResponseEntity<?> findAllUser(@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String fragment){
		if(fragment != null && !fragment.isEmpty()) {
			List<User> test = userService.findAllTitle(fragment,page,size);
			List<UserPermissionAsignedDTO> Content = new ArrayList<UserPermissionAsignedDTO>();
			List<Permission> permission = permissionService.findAll();
			
			for (User user : test) {
				
				List<PermissionVerificationDTO> ListPermission = new ArrayList<PermissionVerificationDTO>();
				
				for (Permission permissiondata : permission) {
					List<UserXPermission> listOfPermissionOwner= user.getUserXpermission();
					PermissionVerificationDTO data = new PermissionVerificationDTO();
					
					boolean flag = true;
					
					for (UserXPermission permission2 : listOfPermissionOwner) {
							if(permissiondata.getCode() == permission2.getPermission().getCode()) {
								data.setCode(permission2.getCode());
								data.setPermission(permission2.getPermission());
								data.setStatus(true);
								ListPermission.add(data);
								flag = false;
							}
					}
					
					if(flag) {
						data.setCode(null);
						data.setPermission(permissiondata);
						data.setStatus(false);
						ListPermission.add(data);
					}

				}
				
				
				
				
				UserPermissionAsignedDTO userPermission = new UserPermissionAsignedDTO(
						user.getCode(),
						user.getName(),
						user.getUsername(),
						user.getEmail(),
						user.getActive(),
						ListPermission
						);
				Content.add(userPermission);
			}
			
			Page<UserPermissionAsignedDTO> users = userService.getPaginatedUsers(Content, page, size);
			
			return new ResponseEntity<>(new PageDTO<UserPermissionAsignedDTO>(
					users.getContent(),
					users.getNumber(),
					users.getSize(),
					users.getTotalElements(),
					users.getTotalPages())
					,HttpStatus.OK);
		}
		
		List<User> test = userService.findAll();
		List<UserPermissionAsignedDTO> Content = new ArrayList<UserPermissionAsignedDTO>();
		List<Permission> permission = permissionService.findAll();
		for (User user : test) {
			
			List<PermissionVerificationDTO> ListPermission = new ArrayList<PermissionVerificationDTO>();
			
			for (Permission permissiondata : permission) {
				List<UserXPermission> listOfPermissionOwner= user.getUserXpermission();
				PermissionVerificationDTO data = new PermissionVerificationDTO();
				
				boolean flag = true;
				
				for (UserXPermission permission2 : listOfPermissionOwner) {
						if(permissiondata.getCode() == permission2.getPermission().getCode()) {
							data.setCode(permission2.getCode());
							data.setPermission(permission2.getPermission());
							data.setStatus(true);
							ListPermission.add(data);
							flag = false;
						}
				}
				
				if(flag) {
					data.setCode(null);
					data.setPermission(permissiondata);
					data.setStatus(false);
					ListPermission.add(data);
				}

			}
			
			
			
			
			UserPermissionAsignedDTO userPermission = new UserPermissionAsignedDTO(
					user.getCode(),
					user.getName(),
					user.getUsername(),
					user.getEmail(),
					user.getActive(),
					ListPermission
					);
			Content.add(userPermission);
		}
		
		Page<UserPermissionAsignedDTO> users = userService.getPaginatedUsers(Content, page, size);
		
		return new ResponseEntity<>(new PageDTO<UserPermissionAsignedDTO>(
				users.getContent(),
				users.getNumber(),
				users.getSize(),
				users.getTotalElements(),
				users.getTotalPages())
				,HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> findOneUser(@PathVariable(name = "id") String code) {
		User user = userService.findOneById(code);
		if(user == null) {
			return new ResponseEntity<>(new MessageDTO("User Not Found"),HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(user,HttpStatus.OK);
	}
	
	@GetMapping("/ticket")
	public ResponseEntity<?> findAllTicketUser(HttpServletRequest request) {
		String tokenHeader = request.getHeader("Authorization");
    	String token = tokenHeader.substring(7);
    	
    	
		User user = userService.findOneByIdentifier(jwtTools.getUsernameFrom(token));
		if(user == null) {
			return new ResponseEntity<>(new MessageDTO("User Not Found"),HttpStatus.NOT_FOUND);
		}
		
		List<ShowTicketDTO> tickets = new ArrayList<>();
		for (Ticket data : user.getTickets()) {
			ShowTicketDTO ticket = new ShowTicketDTO(
					data.getCode(),
					new ShowUserDTO(data.getUser().getCode(),data.getUser().getName(),data.getUser().getUsername(),data.getUser().getEmail()),
					data.getLocality(),
					data.getPurchaseDate()
					);
			tickets.add(ticket);
        }
        
		
		return new ResponseEntity<>(tickets,HttpStatus.OK);
	}
	
	@PatchMapping("/update")
	public ResponseEntity<?> updateUser(@ModelAttribute @Valid UpdateUserDTO info, BindingResult validations){
		if(validations.hasErrors()) {
			return new ResponseEntity<>(errorHandler.mapErrors(validations.getFieldErrors()),HttpStatus.BAD_REQUEST);
		}
		User user = userService.findOneById(info.getUserId());
		if(user == null) {
			return new ResponseEntity<>(new MessageDTO("User Not Found"),HttpStatus.NOT_FOUND);
		}
		if(info.getName().isBlank() && info.getUsername().isBlank()){
			return new ResponseEntity<>(new MessageDTO("Data empty"),HttpStatus.BAD_REQUEST);
		}
		
		List<User> response = userService.findAll();
		
		boolean flag = false;
		for (int i = 0; i< response.size(); i++) {
			if(response.get(i).getUsername().equals(info.getUsername())) {
				flag = true;
			}
		}
		if (flag) {
	        return new ResponseEntity<>(
	                new MessageDTO("Username already exists"), HttpStatus.CONFLICT);
	    }
		
		try {
			userService.updateUser(user,info);
			return new ResponseEntity<>(
					new MessageDTO("User updated"), HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new MessageDTO("Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@PatchMapping("/change-password")
	public ResponseEntity<?> savePassword(@ModelAttribute @Valid ChangePasswordDTO info, BindingResult validations,HttpServletRequest request){
		if(validations.hasErrors()) {
			return new ResponseEntity<>(errorHandler.mapErrors(validations.getFieldErrors()),HttpStatus.BAD_REQUEST);
		}
		String tokenHeader = request.getHeader("Authorization");
    	String token = tokenHeader.substring(7);
		User user = userService.findOneByIdentifier(jwtTools.getUsernameFrom(token));
		
		if(user == null) {
			return new ResponseEntity<>(new MessageDTO("User Not Found"),HttpStatus.NOT_FOUND);
		}
		
		if(user.getPassword() != null && !user.getPassword().isEmpty()) {
			if(!userService.comparePassword(info.getNewPassword(), user.getPassword())) {
				return new ResponseEntity<>(new MessageDTO("The new password must be different from the old one"),HttpStatus.NOT_ACCEPTABLE);
			}
		}
		
	    if (!isStrongPassword(info.getNewPassword())) {
	        return new ResponseEntity<>(
	                new MessageDTO("Invalid password. It should have at least 8 characters and include 1 uppercase, 1 lowercase, 1 number, and 1 special character."),
	                HttpStatus.BAD_REQUEST);
	    }
		
		try {
			userService.updatePasswordUser(user,info);
			return new ResponseEntity<>(
					new MessageDTO("Password updated"), HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new MessageDTO("Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	private boolean isStrongPassword(String password) {
		   
	    if (password.length() < 8) {
	        return false;
	    }

	    boolean hasUppercase = false;
	    boolean hasLowercase = false;
	    boolean hasNumber = false;
	    boolean hasSpecialCharacter = false;

	    for (char ch : password.toCharArray()) {
	        if (Character.isUpperCase(ch)) {
	            hasUppercase = true;
	        } else if (Character.isLowerCase(ch)) {
	            hasLowercase = true;
	        } else if (Character.isDigit(ch)) {
	            hasNumber = true;
	        } else {
	      
	            hasSpecialCharacter = true;
	        }
	    }

	    return hasUppercase && hasLowercase && hasNumber && hasSpecialCharacter;
	}
	
	@GetMapping("/permission/{id}")
	public ResponseEntity<?> findAllPermission(@PathVariable(name = "id") String code) {
		User user = userService.findOneById(code);
		if(user == null) {
			return new ResponseEntity<>(new MessageDTO("User Not Found"),HttpStatus.NOT_FOUND);
		}
		
		List<UserXPermission> permissions = user.getUserXpermission();
		return new ResponseEntity<>(permissions,HttpStatus.OK);
	}
	
	@PostMapping("/permission")
    public ResponseEntity<?> assignPermission(@ModelAttribute @Valid UserPermissionDTO info, BindingResult validations) {

		if(validations.hasErrors()) {
			return new ResponseEntity<>(errorHandler.mapErrors(validations.getFieldErrors()),HttpStatus.BAD_REQUEST);
		}
		User user = userService.findOneById(info.getUser());
		if(user == null) {
			return new ResponseEntity<>(new MessageDTO("User Not Found"),HttpStatus.NOT_FOUND);
		}
		Permission permission = permissionService.findOneById(info.getPermission());
		if(permission == null) {
			return new ResponseEntity<>(new MessageDTO("Permission Not Found"),HttpStatus.NOT_FOUND);
		}
		List<UserXPermission> permissions = user.getUserXpermission();
		Boolean flag = false;
		for (UserXPermission data : permissions) {
			if (data.getPermission().equals(permission)){
				flag = true;
			}
        }
		if(flag == true) {
			return new ResponseEntity<>(new MessageDTO("Permission is Duplicated"),HttpStatus.CONFLICT);
		}
		
		try {
			userXpermissionService.save(user,permission);
			return new ResponseEntity<>(
					new MessageDTO("User Permission assigned"), HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new MessageDTO("Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
    }
	
	@DeleteMapping("/permission/{id}")
    public ResponseEntity<?> removePermission(@PathVariable(name = "id") String code) {
		UserXPermission permission = userXpermissionService.findOneById(code);
		if(permission == null) {
			return new ResponseEntity<>(new MessageDTO("Permission Not Found"),HttpStatus.NOT_FOUND);
		}
		try {
			userXpermissionService.deleteById(code);
			return new ResponseEntity<>(
					new MessageDTO("User Permission Deleted"), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(
					new MessageDTO("Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable(name = "id") String code){
		User user = userService.findOneById(code);
		if(user == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		try {
			userService.deleteById(code);
			return new ResponseEntity<>(
					new MessageDTO("User Deleted"), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(
					new MessageDTO("Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
}
