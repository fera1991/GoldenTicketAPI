package com.group15.goldenticket.services;

import java.util.List;

import org.springframework.data.domain.Page;

import com.group15.goldenticket.models.dtos.SaveUserDTO;
import com.group15.goldenticket.models.dtos.UpdateUserDTO;
import com.group15.goldenticket.models.entities.Token;
import com.group15.goldenticket.models.entities.User;



public interface  UserService {
	//void Register()throws Exception;
	//void Login()throws Exception;
	void save(SaveUserDTO info)throws Exception;
	void deleteById(String id) throws Exception;
	void updatePasswordUser(User user,String newPassword) throws Exception;
	void deactivateUser(User user) throws Exception;
	void updateUser(User user, UpdateUserDTO info);
	User findOneById(String id);
	List<User> findAll();
	Page<User> findAll(int page, int size);
	Page<User> findAllTitle(String title,int page, int size);
	Boolean comparePassword(String toCompare, String current);
	User findOneByIdentifier(String identifier);
	User findOneByUsernameOrEmail(String username, String email);
	
	//Token management
	Token registerToken(User user) throws Exception;
	Boolean isTokenValid(User user, String token);
	void cleanTokens(User user) throws Exception;
	
	//Find User authenticated
	User findUserAuthenticated();
	
}
