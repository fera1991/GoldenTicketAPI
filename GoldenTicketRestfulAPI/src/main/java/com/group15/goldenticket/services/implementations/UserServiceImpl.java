package com.group15.goldenticket.services.implementations;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.group15.goldenticket.models.dtos.SaveUserDTO;
import com.group15.goldenticket.models.dtos.UpdateUserDTO;
import com.group15.goldenticket.models.entities.User;
import com.group15.goldenticket.repositories.UserRepository;
import com.group15.goldenticket.services.UserService;

import jakarta.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService{
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	public PasswordEncoder passwordEncoder;

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
	public void save(SaveUserDTO info) throws Exception {
		User user = new User(
				info.getName(),
				info.getUsername(),
				info.getEmail(),
				passwordEncoder.encode(info.getPassword()),
				true
				);
		userRepository.save(user);
	}

	@Override
	@Transactional(rollbackOn = Exception.class)
	public void updatePasswordUser(User user, String newPassword) throws Exception {
		User updateUser = user;
		updateUser.setPassword(passwordEncoder.encode(newPassword));
		userRepository.save(updateUser);
	}

	@Override
	public void deactivateUser(User user) throws Exception {
		User updateUser = user;
		updateUser.setStatus(false);
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
	public Page<User> findAllTitle(String fragment, int page, int size) {
		Pageable pageable = PageRequest.of(page,size);
		return userRepository.findByUsernameOrEmailContaining(fragment,fragment,pageable);
	}

	@Override
	public List<User> findAll() {
		return userRepository.findAll();
	}
}
