package com.group15.goldenticket.repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.group15.goldenticket.models.entities.Event;


public interface  EventRepository extends JpaRepository<Event, UUID> {
	Page<Event> findByTitleContaining(String fragment, Pageable pageable);
}
