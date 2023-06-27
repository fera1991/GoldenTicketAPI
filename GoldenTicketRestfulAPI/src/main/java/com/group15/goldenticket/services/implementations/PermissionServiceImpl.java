package com.group15.goldenticket.services.implementations;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.group15.goldenticket.models.dtos.SavePermissionDTO;
import com.group15.goldenticket.models.entities.Permission;
import com.group15.goldenticket.repositories.PermissionRepository;
import com.group15.goldenticket.services.PermissionService;

import jakarta.transaction.Transactional;

@Service
public class PermissionServiceImpl implements PermissionService{
	@Autowired
	PermissionRepository permissionRepository;

	@Override
	@Transactional(rollbackOn = Exception.class)
	public void deleteById(String id) throws Exception {
		UUID code = UUID.fromString(id);
		permissionRepository.deleteById(code);
	}

	@Override
	public Permission findOneById(String id) {
		try {
			UUID code = UUID.fromString(id);
			return permissionRepository.findById(code).orElse(null);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public List<Permission> findAll() {
		return permissionRepository.findAll();
	}

	@Override
	@Transactional(rollbackOn = Exception.class)
	public void save(SavePermissionDTO info) throws Exception {
		Permission permission = new Permission(
				info.getPermission()
				);
		permissionRepository.save(permission);
	}
}
