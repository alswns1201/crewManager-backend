package com.crewManager.pro.event.dto;

import com.crewManager.pro.event.domain.Event;
import com.crewManager.pro.event.domain.EventType;
import com.crewManager.pro.event.domain.Location;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CreateEventRequest {
    private String userEmail;
    private String title;
    private Location location;
    private String description;
    private EventType eventType;
    private LocalDateTime eventDateTime;
}
