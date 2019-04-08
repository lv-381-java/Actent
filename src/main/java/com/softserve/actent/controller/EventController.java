package com.softserve.actent.controller;

import com.softserve.actent.constant.StringConstants;
import com.softserve.actent.constant.UrlConstants;
import com.softserve.actent.exceptions.codes.ExceptionCode;
import com.softserve.actent.exceptions.validation.ValidationException;
import com.softserve.actent.model.dto.IdDto;
import com.softserve.actent.model.dto.converter.EventConverter;
import com.softserve.actent.model.dto.converter.EventCreationConverter;
import com.softserve.actent.model.dto.event.EventCreationDto;
import com.softserve.actent.model.dto.event.EventDto;
import com.softserve.actent.model.dto.event.EventFilterDto;
import com.softserve.actent.model.dto.event.EventUpdateDto;
import com.softserve.actent.model.entity.Event;
import com.softserve.actent.repository.EventFilterRepository;
import com.softserve.actent.service.EventService;
import com.softserve.actent.service.impl.EventSpecification;
import org.hibernate.validator.constraints.Length;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@Validated
@RestController
@RequestMapping(UrlConstants.API_V1)
@PreAuthorize("permitAll()")
public class EventController {

    private static final String url = "/events";
    private final EventService eventService;
    private final EventCreationConverter eventCreationConverter;
    private final EventConverter eventConverter;
    private final ModelMapper modelMapper;
    private final EventFilterRepository eventFilterRepository;

    @Autowired
    public EventController(EventService eventService,
                           EventCreationConverter eventCreationConverter,
                           EventConverter eventConverter,
                           ModelMapper modelMapper,
                           EventFilterRepository eventFilterRepository) {

        this.eventService = eventService;
        this.eventCreationConverter = eventCreationConverter;
        this.eventConverter = eventConverter;
        this.modelMapper = modelMapper;
        this.eventFilterRepository = eventFilterRepository;
    }

    @GetMapping(value = url + "/all")
    @ResponseStatus(HttpStatus.OK)
    public List<EventDto> getActiveEvents() {

        List<Event> eventList = eventService.findActiveEvents();
        return eventConverter.convertToDto(eventList);
    }

    @GetMapping(value = url + "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EventDto getEventById(@PathVariable
                                 @NotNull(message = StringConstants.EVENT_ID_CAN_NOT_BE_NULL)
                                 @Positive(message = StringConstants.EVENT_ID_MUST_BE_POSITIVE_AND_GREATER_THAN_ZERO)
                                         Long id) {

        Event event = eventService.get(id);
        return eventConverter.convertToDto(event);
    }

    @PostMapping(value = url + "/filter")
    public List<EventDto> getEventsWithFilter(
            @RequestBody EventFilterDto eventFilterDto) {
        System.out.println(eventFilterDto);
        List<Event> result = eventFilterRepository.findAll(EventSpecification.getTitle(eventFilterDto.getTitle())
                .and(EventSpecification.getCategory(eventFilterDto.getCategoriesId()))
                .and(EventSpecification.getCity(eventFilterDto.getCityName()))
                .and(EventSpecification.getDate(eventFilterDto.getDateFrom(), eventFilterDto.getDateTo())));
        return eventConverter.convertToDto(result);
    }

    @GetMapping(value = url + "/title/{title}")
    @ResponseStatus(HttpStatus.OK)
    public List<EventDto> getByTitle(@PathVariable
                                     @NotNull(message = StringConstants.TITLE_SHOULD_NOT_BE_BLANK)
                                     @Length(message = StringConstants.TITLE_LENGTH_IS_TO_LONG)
                                             String title) {

        if (title == null || title.isEmpty()) {
            throw new ValidationException(StringConstants.TITLE_SHOULD_NOT_BE_BLANK, ExceptionCode.VALIDATION_FAILED);
        }

        List<Event> eventList = eventService.getByTitle(title);
        return eventConverter.convertToDto(eventList);
    }

    @PostMapping(value = url)
//    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.CREATED)
    public IdDto addEvent(@Validated @RequestBody EventCreationDto eventCreationDto) {

        Event event = eventCreationConverter.convertToEntity(eventCreationDto);
        event = eventService.add(event);

        return new IdDto(event.getId());
    }

    @PutMapping(value = url + "/{id}")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.OK)
    public EventDto updateEventById(@Validated @RequestBody EventUpdateDto eventUpdateDto,
                                    @PathVariable
                                    @NotNull(message = StringConstants.EVENT_ID_CAN_NOT_BE_NULL)
                                    @Positive(message = StringConstants.EVENT_ID_MUST_BE_POSITIVE_AND_GREATER_THAN_ZERO)
                                            Long id) {

        Event event = modelMapper.map(eventUpdateDto, Event.class);
        event = eventService.update(event, id);

        return eventConverter.convertToDto(event);
    }

    @DeleteMapping(value = url + "/{id}")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.OK)
    public void deleteEventById(@PathVariable
                                @NotNull(message = StringConstants.EVENT_ID_CAN_NOT_BE_NULL)
                                @Positive(message = StringConstants.EVENT_ID_MUST_BE_POSITIVE_AND_GREATER_THAN_ZERO)
                                        Long id) {

        eventService.delete(id);
    }
}
