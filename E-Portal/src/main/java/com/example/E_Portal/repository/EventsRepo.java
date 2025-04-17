package com.example.e_portal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.e_portal.model.Events;

@Repository
public interface EventsRepo extends JpaRepository<Events, Integer> {
	
}
