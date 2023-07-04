package com.group15.goldenticket.services;

import java.util.List;


import org.springframework.data.domain.Page;

import com.group15.goldenticket.models.dtos.ChangePasswordDTO;
import com.group15.goldenticket.models.dtos.RegisterDTO;
import com.group15.goldenticket.models.dtos.SaveUserDTO;
import com.group15.goldenticket.models.dtos.ShowTicketDTO;
import com.group15.goldenticket.models.dtos.UpdateUserDTO;
import com.group15.goldenticket.models.dtos.UserPermissionAsignedDTO;
import com.group15.goldenticket.models.entities.Event;
import com.group15.goldenticket.models.entities.Token;
import com.group15.goldenticket.models.entities.User;




public interface  UserService {
	User register(RegisterDTO info) throws Exception;
	void save(SaveUserDTO info)throws Exception;
	void deleteById(String id) throws Exception;
	void updatePasswordUser(User user,ChangePasswordDTO info) throws Exception;
	void deactivateUser(User user) throws Exception;
	void updateUser(User user, UpdateUserDTO info);
	User findOneById(String id);
	List<User> findAll();
	Page<User> findAll(int page, int size);
	List<User> findAllTitle(String title,int page, int size);
	Boolean comparePassword(String toCompare, String current);
	User findOneByIdentifier(String identifier);
	User findOneByUsernameOrEmail(String username, String email);
	
	//Token management
	Token registerToken(User user) throws Exception;
	Boolean isTokenValid(User user, String token);
	void cleanTokens(User user) throws Exception;
	
	//Find User authenticated
	User findUserAuthenticated();
	
	Page<ShowTicketDTO> getPaginatedList(List<ShowTicketDTO> list, int page, int size);
	Page<UserPermissionAsignedDTO> getPaginatedUsers(List<UserPermissionAsignedDTO> list, int page, int size);
	
}
