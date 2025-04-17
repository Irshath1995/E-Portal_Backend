package com.example.e_portal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.e_portal.dto.EventsDto;
import com.example.e_portal.service.EventsService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/event")
public class EventsController {
	
	@Autowired
	private EventsService eventsService;
	
	@PostMapping("/addEvent")
	public ResponseEntity<EventsDto> addEvents(@Valid @RequestBody EventsDto eventsDto){
		EventsDto events = eventsService.addEvents(eventsDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(events);
	}
	
	@GetMapping("/getAllEvents")
	public ResponseEntity<List<EventsDto>> getAllEvents(){
		List<EventsDto> events = eventsService.getAllEvents();
		return ResponseEntity.status(HttpStatus.OK).body(events);
	}
	
	@DeleteMapping("/{id}/deleteEvents")
	public ResponseEntity<String> deleteEvents(@PathVariable("id") Integer id){
		Boolean isEventDeleted = eventsService.deleteEvents(id);
		if(isEventDeleted)
			return ResponseEntity.status(HttpStatus.OK).body("Events deleted Successfully.");
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting events");
	}
	
	@PutMapping("/{id}/updateEvent")
	public ResponseEntity<?> updateEvent(@PathVariable("id") Integer id,@Valid @RequestBody EventsDto eventsDto){
		ResponseEntity<?> updatedEvent = eventsService.updateEvent(id, eventsDto);
		return updatedEvent;
	}

}
