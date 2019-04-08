package com.softserve.actent.controller;

import com.softserve.actent.constant.StringConstants;
import com.softserve.actent.constant.UrlConstants;
import com.softserve.actent.model.dto.IdDto;
import com.softserve.actent.model.dto.converter.EventUserConverter;
import com.softserve.actent.model.dto.converter.EventUserFilterConverter;
import com.softserve.actent.model.dto.eventUser.EventUserDto;
import com.softserve.actent.model.dto.eventUser.EventUserFilterDto;
import com.softserve.actent.model.entity.EventUser;
import com.softserve.actent.model.entity.EventUserType;
import com.softserve.actent.repository.UserEventsFilterRepository;
import com.softserve.actent.service.EventUserService;
import com.softserve.actent.service.impl.UserEventsSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@Validated
@RestController
@RequestMapping(UrlConstants.API_V1)
@PreAuthorize("permitAll()")
public class EventUserController {

    private static final String url = "/eventsUsers";
    private final EventUserService eventUserService;
    private final EventUserConverter eventsUsersConverter;
    private final UserEventsFilterRepository filterRepository;
    private final EventUserFilterConverter filterConverter;

    @Autowired
    public EventUserController(EventUserService eventUserService, EventUserConverter eventUserConverter, UserEventsFilterRepository filterRepository, EventUserFilterConverter filterConverter) {
        this.eventUserService = eventUserService;
        this.eventsUsersConverter = eventUserConverter;
        this.filterRepository = filterRepository;
        this.filterConverter = filterConverter;
    }

    @PostMapping(value = url)
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.CREATED)
    public IdDto addEventUser(@Validated @RequestBody EventUserDto eventUserDto) {

        EventUser eventUser = eventsUsersConverter.convertToEntity(eventUserDto);
        return new IdDto(eventUserService.add(eventUser).getId());
    }

    @GetMapping(value = url)
    @ResponseStatus(HttpStatus.OK)
    public List<EventUserDto> getAllEventUser() {

        List<EventUser> eventUserList = eventUserService.getAll();
        return eventsUsersConverter.convertToDto(eventUserList);
    }

    @GetMapping(value = url + "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EventUserDto getEventUserById(@PathVariable
                                         @NotNull(message = StringConstants.EVENT_USER_ID_CAN_NOT_BE_NULL)
                                         @Positive(message = StringConstants.EVENT_ID_MUST_BE_POSITIVE_AND_GREATER_THAN_ZERO)
                                                 Long id) {

        EventUser eventUser = eventUserService.get(id);
        return eventsUsersConverter.convertToDto(eventUser);
    }

    @GetMapping(value = url + "/events/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<EventUserDto> getEventUserByEventId(@PathVariable
                                                    @NotNull(message = StringConstants.EVENT_ID_CAN_NOT_BE_NULL)
                                                    @Positive(message = StringConstants.EVENT_ID_MUST_BE_POSITIVE_AND_GREATER_THAN_ZERO)
                                                            Long id) {

        List<EventUser> eventUserList = eventUserService.getByEventId(id);
        return eventsUsersConverter.convertToDto(eventUserList);
    }

    @PutMapping(value = url + "/{id}")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.OK)
    public EventUserDto updateEventsUsers(@Validated @RequestBody EventUserDto eventUserDto,
                                          @PathVariable
                                          @NotNull(message = StringConstants.EVENT_USER_ID_CAN_NOT_BE_NULL)
                                          @Positive(message = StringConstants.EVENT_ID_MUST_BE_POSITIVE_AND_GREATER_THAN_ZERO)
                                                  Long id) {

        EventUser eventUser = eventsUsersConverter.convertToEntity(eventUserDto);
        eventUser = eventUserService.update(eventUser, id);
        return eventsUsersConverter.convertToDto(eventUser);
    }

    @DeleteMapping(value = url + "/{id}")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.OK)
    public void deleteById(@PathVariable
                           @NotNull(message = StringConstants.EVENT_USER_ID_CAN_NOT_BE_NULL)
                           @Positive(message = StringConstants.EVENT_ID_MUST_BE_POSITIVE_AND_GREATER_THAN_ZERO)
                                   Long id) {

        eventUserService.delete(id);
    }

    @GetMapping(value = url + "/allEvents/{userId}")
    public List<EventUserFilterDto> getAllFilter(@PathVariable @NotNull(message = StringConstants.USER_ID_CAN_NOT_BE_NULL)
                                                 @Positive(message = StringConstants.USER_ID_MUST_BE_POSITIVE_AND_GREATER_THAN_ZERO) Long userId,
                                                 @RequestParam(name = "city", required = false) String city,
                                                 @RequestParam(name = "userType", required = false) EventUserType userType,
                                                 @RequestParam(name = "category", required = false) String category) {
        List<EventUser> result = filterRepository.findAll(UserEventsSpecification.getUserId(userId)
                .and(UserEventsSpecification.getCity(city))
                .and(UserEventsSpecification.getUserType(userType))
                .and(UserEventsSpecification.getCategory(category)));

        return filterConverter.convertToDto(result);
    }

    @GetMapping(value = url + "/pastEvents/{userId}")
    public List<EventUserFilterDto> getAllPastEventsFilter(@PathVariable @NotNull(message = StringConstants.USER_ID_CAN_NOT_BE_NULL)
                                                           @Positive(message = StringConstants.USER_ID_MUST_BE_POSITIVE_AND_GREATER_THAN_ZERO) Long userId,
                                                           @RequestParam(name = "city", required = false) String city,
                                                           @RequestParam(name = "userType", required = false) EventUserType userType,
                                                           @RequestParam(name = "category", required = false) String category) {
        List<EventUser> result = filterRepository.findAll(UserEventsSpecification.getUserIdAndPastEvents(userId)
                .and(UserEventsSpecification.getCity(city))
                .and(UserEventsSpecification.getUserType(userType))
                .and(UserEventsSpecification.getCategory(category)));

        return filterConverter.convertToDto(result);
    }

    @GetMapping(value = url + "/futureEvents/{userId}")
    public List<EventUserFilterDto> getAllFutureEventsFilter(@PathVariable @NotNull(message = StringConstants.USER_ID_CAN_NOT_BE_NULL)
                                                             @Positive(message = StringConstants.USER_ID_MUST_BE_POSITIVE_AND_GREATER_THAN_ZERO) Long userId,
                                                             @RequestParam(name = "city", required = false) String city,
                                                             @RequestParam(name = "userType", required = false) EventUserType userType,
                                                             @RequestParam(name = "category", required = false) String category) {
        List<EventUser> result = filterRepository.findAll(UserEventsSpecification.getUserIdAndFutureEvents(userId)
                .and(UserEventsSpecification.getCity(city))
                .and(UserEventsSpecification.getUserType(userType))
                .and(UserEventsSpecification.getCategory(category)));

        return filterConverter.convertToDto(result);
    }

}
