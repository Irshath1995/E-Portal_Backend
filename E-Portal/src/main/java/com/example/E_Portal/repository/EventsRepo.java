package com.example.E_Portal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.E_Portal.model.Events;

@Repository
public interface EventsRepo extends JpaRepository<Events, Integer> {
	
}
