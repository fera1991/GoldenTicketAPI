package com.group15.goldenticket.services;

import java.util.List;

import com.group15.goldenticket.models.entities.Permission;
import com.group15.goldenticket.models.entities.User;
import com.group15.goldenticket.models.entities.UserXPermission;

public interface  UserXPermissionService {
	void save(User user, Permission permission)throws Exception;
	void deleteById(String id) throws Exception;
	UserXPermission findOneById(String id);
	List<UserXPermission> findAll();
}
