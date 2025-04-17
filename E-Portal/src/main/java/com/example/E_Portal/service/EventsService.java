package com.example.e_portal.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.example.e_portal.dto.EventsDto;

public interface EventsService {
	
	EventsDto addEvents(EventsDto eventsDto);

	List<EventsDto> getAllEvents();

	Boolean deleteEvents(Integer id);

	ResponseEntity<?> updateEvent(Integer id, EventsDto eventsDto);

}
