package com.group15.goldenticket.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.ListCrudRepository;

import com.group15.goldenticket.models.entities.Token;
import com.group15.goldenticket.models.entities.User;

public interface TokenRepository 
extends ListCrudRepository<Token, UUID>{ 

List<Token> findByUserAndActive(User user, Boolean active);

}