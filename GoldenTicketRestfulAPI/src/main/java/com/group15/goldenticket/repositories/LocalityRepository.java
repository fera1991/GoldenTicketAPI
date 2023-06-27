package com.group15.goldenticket.repositories;

import java.util.UUID;

import org.springframework.data.repository.ListCrudRepository;

import com.group15.goldenticket.models.entities.Locality;

public interface  LocalityRepository extends ListCrudRepository<Locality, UUID> {

}
