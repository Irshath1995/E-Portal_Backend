package com.example.E_Portal.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.example.E_Portal.dto.EventsDto;

public interface EventsService {
	
	EventsDto addEvents(EventsDto eventsDto);

	List<EventsDto> getAllEvents();

	Boolean deleteEvents(Integer id);

	ResponseEntity<?> updateEvent(Integer id, EventsDto eventsDto);

}
