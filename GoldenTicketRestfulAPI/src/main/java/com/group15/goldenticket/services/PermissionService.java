package com.group15.goldenticket.services;

import java.util.List;

import com.group15.goldenticket.models.dtos.SavePermissionDTO;
import com.group15.goldenticket.models.entities.Permission;

public interface  PermissionService {
	void save(SavePermissionDTO info)throws Exception;
	void deleteById(String id) throws Exception;
	Permission findOneById(String id);
	List<Permission> findAll();
}
