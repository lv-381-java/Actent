package com.softserve.actent.controller;

import com.softserve.actent.model.dto.converter.EventConverter;
import com.softserve.actent.model.dto.event.EventCreationDto;
import com.softserve.actent.model.entity.Event;
import com.softserve.actent.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class EventController {

    private final EventService eventService;
    private final EventConverter eventConverter;

    @Autowired
    public EventController(EventService eventService, EventConverter eventConverter) {
        this.eventService = eventService;
        this.eventConverter = eventConverter;
    }

    @PostMapping(value = "/events")
    public ResponseEntity<Long> addEvent(@RequestBody EventCreationDto eventCreationDto) {

        Event event = eventConverter.convertToEntity(eventCreationDto);
        event = eventService.add(event);

        return new ResponseEntity<>(event.getId(), HttpStatus.CREATED) ;
    }
}