package com.crewManager.pro.event.controller;


import com.crewManager.pro.event.dto.CreateEventRequest;
import com.crewManager.pro.event.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> createEvent(@RequestBody @Valid CreateEventRequest req){
        Map<String, String> result = new HashMap<>();
        String name = eventService.createEvent(req);
        result.put("name",name);
        return ResponseEntity.ok(result);
    }


}
