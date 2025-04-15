package com.infotel.e_portal.dto;

import java.time.LocalDateTime;

import com.infotel.e_portal.model.Events;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EventsDto {
	
	@NotBlank(message="Events description is mandatory.")
	private String eventDescription;
	
	@NotNull(message="Events date & time is mandatory.")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm") // Accepts input like "2025-04-08 16:00"
	private LocalDateTime eventDateTime;

	
	public static Events toEvents(EventsDto eventsDto) {
		if(eventsDto == null) return null;
		
		Events events = new Events();
		
		events.setEventDescription(eventsDto.getEventDescription());
		events.setEventDateTime(eventsDto.getEventDateTime());
		
		return events;
	}
	
	public static EventsDto toEventsDto(Events events) {
		if(events == null) return null;
		
		EventsDto eventsDto = new EventsDto();
		
		eventsDto.setEventDescription(events.getEventDescription());
		eventsDto.setEventDateTime(events.getEventDateTime());
		
		return eventsDto;
	}
}
