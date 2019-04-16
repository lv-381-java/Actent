package com.softserve.actent.controller;

import com.softserve.actent.constant.StringConstants;
import com.softserve.actent.constant.UrlConstants;
import com.softserve.actent.exceptions.codes.ExceptionCode;
import com.softserve.actent.exceptions.validation.ValidationException;
import com.softserve.actent.model.dto.IdDto;
import com.softserve.actent.model.dto.converter.UltraEventConverter;
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
    private final ModelMapper modelMapper;
    private final UltraEventConverter ultraEventConverter;
    private final EventFilterRepository eventFilterRepository;

    @Autowired
    public EventController(EventService eventService,
                           ModelMapper modelMapper,
                           UltraEventConverter ultraEventConverter,
                           EventFilterRepository eventFilterRepository) {

        this.eventService = eventService;
        this.modelMapper = modelMapper;
        this.ultraEventConverter = ultraEventConverter;
        this.eventFilterRepository = eventFilterRepository;
    }

    @GetMapping(value = url + "/all")
    @ResponseStatus(HttpStatus.OK)
    public List<UltraEventDto> getActiveEvents() {

        List<Event> eventList = eventService.findActiveEvents();
        return ultraEventConverter.convertToDtoList(eventList, "ostap");
    }

    @GetMapping(value = url + "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UltraEventDto getEventById(@PathVariable
                                 @NotNull(message = StringConstants.EVENT_ID_CAN_NOT_BE_NULL)
                                 @Positive(message = StringConstants.EVENT_ID_MUST_BE_POSITIVE_AND_GREATER_THAN_ZERO) Long id) {

        Event event = eventService.get(id);
        return ultraEventConverter.convertToDto(event, "full");
    }

    @PostMapping(value = url + "/filter")
    public List<UltraEventDto> getEventsWithFilter(@RequestBody EventFilterDto eventFilterDto) {
        System.out.println(eventFilterDto);
        List<Event> result = eventFilterRepository.findAll(EventSpecification.getTitle(eventFilterDto.getTitle())
                .and(EventSpecification.getCategory(eventFilterDto.getCategoriesId()))
                .and(EventSpecification.getCity(eventFilterDto.getCityName()))
                .and(EventSpecification.getDate(eventFilterDto.getDateFrom(), eventFilterDto.getDateTo())));
        return ultraEventConverter.convertToDtoList(result, "ostap");
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
        return ultraEventConverter.convertToDtoList(eventList, "ostap");
    }

    @PostMapping(value = url)
//    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.CREATED)
    public IdDto addEvent(@Validated @RequestBody EventCreationDto eventCreationDto) {

        Event event = ultraEventConverter.convertToEntity(eventCreationDto);
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

        return ultraEventConverter.convertToDto(event, "full");
    }

    @DeleteMapping(value = url + "/{id}")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.OK)
    public void deleteEventById(@PathVariable
                                @NotNull(message = StringConstants.EVENT_ID_CAN_NOT_BE_NULL)
                                @Positive(message = StringConstants.EVENT_ID_MUST_BE_POSITIVE_AND_GREATER_THAN_ZERO) Long id) {

        eventService.delete(id);
    }

    @GetMapping(value = url + "/all/{type}")
    @ResponseStatus(HttpStatus.OK)
    public UltraEventDto getAllWithType(@PathVariable(value = "type", required = false) String type) {

        List<Event> eventList = eventService.getAll();

        return ultraEventConverter.convertToDto(eventList.get(0), type);
    }

}
