package com.group15.goldenticket.services.implementations;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.group15.goldenticket.models.entities.Permission;
import com.group15.goldenticket.models.entities.User;
import com.group15.goldenticket.models.entities.UserXPermission;
import com.group15.goldenticket.repositories.UserXPermissionRepository;
import com.group15.goldenticket.services.UserXPermissionService;

import jakarta.transaction.Transactional;

@Service
public class UserXPermissionServiceImpl implements UserXPermissionService{
	@Autowired
	UserXPermissionRepository userXPermissionRepository;

	@Override
	@Transactional(rollbackOn = Exception.class)
	public void deleteById(String id) throws Exception {
		UUID code = UUID.fromString(id);
		userXPermissionRepository.deleteById(code);
		
	}

	@Override
	public UserXPermission findOneById(String id) {
		try {
			UUID code = UUID.fromString(id);
			return userXPermissionRepository.findById(code).orElse(null);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public List<UserXPermission> findAll() {
		return userXPermissionRepository.findAll();
	}

	@Override
	@Transactional(rollbackOn = Exception.class)
	public void save(User user, Permission permission) throws Exception {
		UserXPermission userXpermission = new UserXPermission(
				permission,
				user
				);
		userXPermissionRepository.save(userXpermission);
	}
}
