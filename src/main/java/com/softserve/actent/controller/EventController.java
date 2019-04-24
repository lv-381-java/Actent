package com.softserve.actent.controller;

import com.softserve.actent.constant.StringConstants;
import com.softserve.actent.constant.UrlConstants;
import com.softserve.actent.exceptions.codes.ExceptionCode;
import com.softserve.actent.exceptions.validation.ValidationException;
import com.softserve.actent.model.dto.IdDto;
import com.softserve.actent.model.dto.converter.EventConverter;
import com.softserve.actent.model.dto.converter.EventDtoType;
import com.softserve.actent.model.dto.event.EventCreationDto;
import com.softserve.actent.model.dto.event.EventFilterDto;
import com.softserve.actent.model.dto.event.EventUpdateDto;
import com.softserve.actent.model.dto.event.UltraEventDto;
import com.softserve.actent.model.entity.Event;
import com.softserve.actent.repository.EventFilterRepository;
import com.softserve.actent.service.EventService;
import com.softserve.actent.service.impl.EventSpecification;

import org.hibernate.validator.constraints.Length;
import org.modelmapper.ModelMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

import javax.validation.constraints.NotBlank;
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
    private final ModelMapper modelMapper;
    private final EventConverter eventConverter;
    private final EventFilterRepository eventFilterRepository;

    @Autowired
    public EventController(EventService eventService,
                           ModelMapper modelMapper,
                           EventConverter eventConverter,
                           EventFilterRepository eventFilterRepository) {

        this.eventService = eventService;
        this.modelMapper = modelMapper;
        this.eventConverter = eventConverter;
        this.eventFilterRepository = eventFilterRepository;
    }

    @GetMapping(value = url + "/all/{page}/{size}")
    @ResponseStatus(HttpStatus.OK)
    public List<UltraEventDto> getActiveEvents(@PathVariable
                                          @NotNull(message = StringConstants.EVENT_ID_CAN_NOT_BE_NULL)
                                                  int page,
                                          @PathVariable
                                          @NotNull(message = StringConstants.EVENT_ID_CAN_NOT_BE_NULL)
                                          @Positive(message = StringConstants.EVENT_ID_MUST_BE_POSITIVE_AND_GREATER_THAN_ZERO)
                                                  int size) {

        Page<Event> eventPage = eventService.findActiveEvents(PageRequest.of(page, size));
        System.out.println(eventPage.getTotalElements());
        return eventConverter.convertToDtoList(eventPage.getContent(), EventDtoType.FOR_LIST);
    }

    @GetMapping(value = url + "/totalElements")
    @ResponseStatus(HttpStatus.OK)
    public Long getTotalElements() {

        Page<Event> eventPage = eventService.findActiveEvents(PageRequest.of(0, 1));
        return eventPage.getTotalElements();
    }

    @GetMapping(value = url + "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UltraEventDto getEventById(@PathVariable
                                 @NotNull(message = StringConstants.EVENT_ID_CAN_NOT_BE_NULL)
                                 @Positive(message = StringConstants.EVENT_ID_MUST_BE_POSITIVE_AND_GREATER_THAN_ZERO) Long id) {

        Event event = eventService.get(id);
        return eventConverter.convertToDto(event, EventDtoType.FULL);
    }

    @PostMapping(value = url + "/filter")
    public List<UltraEventDto> getEventsWithFilter(@RequestBody EventFilterDto eventFilterDto) {
        List<Event> result = eventFilterRepository.findAll(EventSpecification.getTitle(eventFilterDto.getTitle())
                .and(EventSpecification.getCategory(eventFilterDto.getCategoriesId()))
                .and(EventSpecification.getLocation(eventFilterDto.getLocationAddress()))
                .and(EventSpecification.getDate(eventFilterDto.getDateFrom(), eventFilterDto.getDateTo())));
        return eventConverter.convertToDtoList(result, EventDtoType.FOR_LIST);
    }

    @GetMapping(value = url + "/title/{title}")
    @ResponseStatus(HttpStatus.OK)
    public List<UltraEventDto> getByTitle(@PathVariable
                                     @NotNull(message = StringConstants.TITLE_SHOULD_NOT_BE_BLANK)
                                     @Length(message = StringConstants.TITLE_LENGTH_IS_TO_LONG) String title) {
        if (title == null || title.isEmpty()) {
            throw new ValidationException(StringConstants.TITLE_SHOULD_NOT_BE_BLANK, ExceptionCode.VALIDATION_FAILED);
        }
        List<Event> eventList = eventService.getByTitle(title);
        return eventConverter.convertToDtoList(eventList, EventDtoType.FOR_LIST);
    }

    @PostMapping(value = url)
    @ResponseStatus(HttpStatus.CREATED)
    public IdDto addEvent(@Validated @RequestBody EventCreationDto eventCreationDto) {
        Event event = eventConverter.convertToEntity(eventCreationDto);
        event = eventService.add(event);
        return new IdDto(event.getId());
    }

    @PutMapping(value = url + "/{id}")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.OK)
    public UltraEventDto updateEventById(@Validated @RequestBody EventUpdateDto eventUpdateDto,
                                         @PathVariable
                                    @NotNull(message = StringConstants.EVENT_ID_CAN_NOT_BE_NULL)
                                    @Positive(message = StringConstants.EVENT_ID_MUST_BE_POSITIVE_AND_GREATER_THAN_ZERO) Long id) {
        Event event = modelMapper.map(eventUpdateDto, Event.class);
        event = eventService.update(event, id);
        return eventConverter.convertToDto(event, EventDtoType.FULL);
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

    @GetMapping(value = url + "/all/{type}")
    @ResponseStatus(HttpStatus.OK)
    public List<UltraEventDto> getAllWithType(@PathVariable(required = false)
                                                  @NotBlank(message = StringConstants.EVENT_DTO_TYPE_CAN_NOT_BE_NULL_OR_BLANK)
                                                          String type) {
        type = type.trim().toUpperCase();
        for (EventDtoType value : EventDtoType.values()) {
            if (value.name().equals(type)) {
                return eventConverter.convertToDtoList(eventService.getAll(), EventDtoType.valueOf(type));
            }
        }
        throw  new ValidationException(StringConstants.EVENT_DTO_TYPE_IS_NOT_EXIST, ExceptionCode.NOT_FOUND);
    }

}
