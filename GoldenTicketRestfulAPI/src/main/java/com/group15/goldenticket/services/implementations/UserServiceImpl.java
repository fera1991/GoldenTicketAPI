package com.group15.goldenticket.services.implementations;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.group15.goldenticket.models.dtos.ChangePasswordDTO;
import com.group15.goldenticket.models.dtos.RegisterDTO;
import com.group15.goldenticket.models.dtos.SaveUserDTO;
import com.group15.goldenticket.models.dtos.ShowTicketDTO;
import com.group15.goldenticket.models.dtos.UpdateUserDTO;
import com.group15.goldenticket.models.dtos.UserPermissionAsignedDTO;
import com.group15.goldenticket.models.entities.Event;
import com.group15.goldenticket.models.entities.Token;
import com.group15.goldenticket.models.entities.User;
import com.group15.goldenticket.repositories.TokenRepository;
import com.group15.goldenticket.repositories.UserRepository;
import com.group15.goldenticket.services.UserService;
import com.group15.goldenticket.utils.JWTTools;


import jakarta.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService{
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	private JWTTools jwtTools;
	
	@Autowired
	public PasswordEncoder passwordEncoder;
	
	@Autowired
	private TokenRepository tokenRepository;

	@Override
	@Transactional(rollbackOn = Exception.class)
	public void deleteById(String id) throws Exception {
		UUID code = UUID.fromString(id);
		userRepository.deleteById(code);
	}

	@Override
	public User findOneById(String id) {
		try {
			UUID code = UUID.fromString(id);
			return userRepository.findById(code).orElse(null);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Boolean comparePassword(String toCompare, String current) {
		return passwordEncoder.matches(toCompare, current);
	}

	@Override
	public User findOneByIdentifier(String identifier) {
		return userRepository.findOneByUsernameOrEmail(identifier, identifier);
	}

	@Override
	public User findOneByUsernameOrEmail(String username, String email) {
		return userRepository.findOneByUsernameOrEmail(username, email);
	}
	
	@Override
	@Transactional(rollbackOn = Exception.class)
	public User register(RegisterDTO info) throws Exception {
		User user = new User(
				info.getName(),
				info.getEmail(),
				info.getEmail(),
				""
				);
		return userRepository.save(user);
	}

	@Override
	@Transactional(rollbackOn = Exception.class)
	public void save(SaveUserDTO info) throws Exception {
		User user = new User(
				info.getName(),
				info.getUsername(),
				info.getEmail(),
				passwordEncoder.encode(info.getPassword())
				);
		userRepository.save(user);
	}

	@Override
	@Transactional(rollbackOn = Exception.class)
	public void updatePasswordUser(User user, ChangePasswordDTO info) throws Exception {
		User updateUser = user;
		updateUser.setPassword(passwordEncoder.encode(info.getNewPassword()));
		userRepository.save(updateUser);
	}

	@Override
	public void deactivateUser(User user) throws Exception {
		User updateUser = user;
		updateUser.setActive(false);
		userRepository.save(updateUser);
	}

	@Override
	@Transactional(rollbackOn = Exception.class)
	public void updateUser(User user, UpdateUserDTO info) {
		User updateUser = user;
		if(!info.getName().isBlank()) {
			updateUser.setName(info.getName());
		}
		if(!info.getUsername().isBlank()) {
			updateUser.setUsername(info.getUsername());
		}
		userRepository.save(updateUser);
	}

	@Override
	public Page<User> findAll(int page, int size) {
		Pageable pageable = PageRequest.of(page,size);
		return userRepository.findAll(pageable);
	}

	@Override
	public List<User> findAllTitle(String fragment, int page, int size) {
		return userRepository.findByUsernameOrEmailContaining(fragment,fragment);
	}

	@Override
	public List<User> findAll() {
		return userRepository.findAll();
	}

	@Override
	@Transactional(rollbackOn = Exception.class)
	public Token registerToken(User user) throws Exception {
		cleanTokens(user);
		
		String tokenString = jwtTools.generateToken(user);
		Token token = new Token(tokenString, user);
		
		tokenRepository.save(token);
		
		return token;
	}

	@Override
	public Boolean isTokenValid(User user, String token) {
		try {
			cleanTokens(user);
			List<Token> tokens = tokenRepository.findByUserAndActive(user, true);
			
			tokens.stream()
				.filter(tk -> tk.getContent().equals(token))
				.findAny()
				.orElseThrow(() -> new Exception());
			
			return true;
		} catch (Exception e) {
			return false;
		}		
	}

	@Override
	@Transactional(rollbackOn = Exception.class)
	public void cleanTokens(User user) throws Exception {
		List<Token> tokens = tokenRepository.findByUserAndActive(user, true);
		
		tokens.forEach(token -> {
			if(!jwtTools.verifyToken(token.getContent())) {
				token.setActive(false);
				tokenRepository.save(token);
			}
		});
		
	}

	@Override
	public User findUserAuthenticated() {
		String username = SecurityContextHolder
			.getContext()
			.getAuthentication()
			.getName();
		
		return userRepository.findOneByUsernameOrEmail(username, username);
	}

	@Override
	public Page<ShowTicketDTO> getPaginatedList(List<ShowTicketDTO> list, int page, int size) {
		int startIndex = page * size;
        int endIndex = Math.min(startIndex + size, list.size());
        
        List<ShowTicketDTO> sublist = list.subList(startIndex, endIndex);
        PageRequest pageRequest = PageRequest.of(page,size);
        
        return new PageImpl<>(sublist,pageRequest,list.size());
	}
	
	@Override
	public Page<UserPermissionAsignedDTO> getPaginatedUsers(List<UserPermissionAsignedDTO> list, int page, int size) {
		int startIndex = page * size;
        int endIndex = Math.min(startIndex + size, list.size());
        
        List<UserPermissionAsignedDTO> sublist = list.subList(startIndex, endIndex);
        PageRequest pageRequest = PageRequest.of(page,size);
        
        return new PageImpl<>(sublist,pageRequest,list.size());
	}
	
	
	

}
	
