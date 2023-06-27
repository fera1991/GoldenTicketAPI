package com.group15.goldenticket.repositories;

import java.util.UUID;

import org.springframework.data.repository.ListCrudRepository;

import com.group15.goldenticket.models.entities.UserXPermission;


public interface  UserXPermissionRepository extends ListCrudRepository<UserXPermission, UUID>{

}
