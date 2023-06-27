package com.group15.goldenticket.repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.group15.goldenticket.models.entities.User;

public interface  UserRepository extends JpaRepository<User, UUID>{
	public User findOneByUsernameOrEmail(String username, String email);
	Page<User> findByUsernameOrEmailContaining(String username,String email, Pageable pageable);
}
