package com.softserve.actent.model.dto.converter;

import com.softserve.actent.constant.ExceptionMessages;
import com.softserve.actent.exceptions.DataNotFoundException;
import com.softserve.actent.exceptions.codes.ExceptionCode;
import com.softserve.actent.model.dto.TagDto;
import com.softserve.actent.model.dto.equipment.EquipmentDto;
import com.softserve.actent.model.dto.event.*;
import com.softserve.actent.model.dto.eventUser.EventUserForEventDto;
import com.softserve.actent.model.entity.*;

import org.modelmapper.ModelMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class EventConverter {

    private final ModelMapper modelMapper;

    @Autowired
    public EventConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public UltraEventDto convertToDto(Event event, EventDtoType type) {
        nullHunter(event, ExceptionMessages.EVENT_CAN_NOT_BE_NULL);
        return getMethod(type).apply(event);
    }
    public Event convertToEntity(EventCreationDto eventCreationDto) {
        nullHunter(eventCreationDto, ExceptionMessages.EVENT_CAN_NOT_BE_NULL);
        return extractEvent(eventCreationDto);
    }

    public List<UltraEventDto> convertToDtoList(List<Event> events, EventDtoType type) {
        nullHunter(events, ExceptionMessages.EVENT_LIST_IS_NULL);
        return getEventDtoList(events, getMethod(type));
    }

    private MinimalEventDto getMinimalEventDto(Event event) {
        nullHunter(event, ExceptionMessages.EVENT_CAN_NOT_BE_NULL);
        return modelMapper.map(event, MinimalEventDto.class);
    }
    private EventOstapDto getForListDto(Event event) {
        nullHunter(event, ExceptionMessages.EVENT_CAN_NOT_BE_NULL);
        EventOstapDto eventOstapDto = modelMapper.map(event, EventOstapDto.class);
        eventOstapDto.setLocationForEventDto(getLocation(event.getAddress()));
        eventOstapDto.setCategoryForEventDto(getCategory(event.getCategory()));
        return eventOstapDto;
    }
    private ShowEventDto getShowEventDto(Event event) {
        nullHunter(event, ExceptionMessages.EVENT_CAN_NOT_BE_NULL);
        ShowEventDto showEventDto = modelMapper.map(event, ShowEventDto.class);
        showEventDto.setLocationForEventDto(getLocation(event.getAddress()));
        showEventDto.setCategoryForEventDto(getCategory(event.getCategory()));
        showEventDto.setChatForEventDto(getChat(event.getChat()));
        showEventDto.setUserForEventDto(getCreator(event.getCreator()));
        return showEventDto;
    }
    private EventFullDto getFullEventDto(Event event) {
        nullHunter(event, ExceptionMessages.EVENT_CAN_NOT_BE_NULL);
        EventFullDto eventFullDto = modelMapper.map(event, EventFullDto.class);
        eventFullDto.setLocationForEventDto(getLocation(event.getAddress()));
        eventFullDto.setCategoryForEventDto(getCategory(event.getCategory()));
        eventFullDto.setChatForEventDto(getChat(event.getChat()));
        eventFullDto.setUserForEventDto(getCreator(event.getCreator()));
        eventFullDto.setEquipments(getEquipments(event));
        eventFullDto.setEventForEventUserDtoList(getEventsUsers(event));
        eventFullDto.setImage(event.getImage());
        eventFullDto.setTags(getTags(event));
        eventFullDto.setFeedback(getReviews(event));
        return eventFullDto;
    }

    private LocationForEventDto getLocation(Location location) {
        return modelMapper.map(location, LocationForEventDto.class);
    }
    private CategoryForEventDto getCategory(Category category) {
        return modelMapper.map(category, CategoryForEventDto.class);
    }
    private ChatForEventDto getChat(Chat chat) {
        return modelMapper.map(chat, ChatForEventDto.class);
    }
    private UserForEventDto getCreator(User user) {
        return modelMapper.map(user, UserForEventDto.class);
    }
    private List<EquipmentDto> getEquipments(Event event) {
        if (event.getEquipments() == null) {
            return null;
        }
        return event.getEquipments().stream()
                .map(equipment -> modelMapper.map(equipment, EquipmentDto.class))
                .collect(Collectors.toList());
    }
    private List<EventUserForEventDto> getEventsUsers(Event event) {

        List<EventUser> eventUserList = event.getEventUserList();
        if (eventUserList == null) {
            return null;
        }

        List<EventUserForEventDto> eventUserForEvenDtoList = new ArrayList<>(eventUserList.size());
        for (EventUser eventUser : eventUserList) {
            eventUserForEvenDtoList.add(getEventUserForEventDto(eventUser));
        }

        return eventUserForEvenDtoList;
    }
    private List<TagDto> getTags(Event event) {
        List<TagDto> tags = null;
        if (event.getTags() != null) {
            tags = event.getTags().stream().map(e -> modelMapper.map(e, TagDto.class)).collect(Collectors.toList());
        }
        return tags;
    }
    private List<ReviewForEventDto> getReviews(Event event) {
        List<ReviewForEventDto> reviews = null;
        if (event.getFeedback() != null) {
            reviews = event.getFeedback().stream().map(this::getReviewDto).collect(Collectors.toList());
        }
        return reviews;
    }
    private ReviewForEventDto getReviewDto(Review review) {
        ReviewForEventDto reviewForEventDto = modelMapper.map(review, ReviewForEventDto.class);
        reviewForEventDto.setAuthor(getCreator(review.getAuthor()));
        return reviewForEventDto;
    }

    private EventUserForEventDto getEventUserForEventDto(EventUser eventUser) {
        EventUserForEventDto eventUserForEvenDto = new EventUserForEventDto();
        eventUserForEvenDto.setId(eventUser.getId());
        eventUserForEvenDto.setUserForEventDto(modelMapper.map(eventUser.getUser(), UserForEventDto.class));
        eventUserForEvenDto.setEventUserType(eventUser.getType());
        return eventUserForEvenDto;
    }
    private Event extractEvent(EventCreationDto eventCreationDto) {
        Event event = modelMapper.map(eventCreationDto, Event.class);
        event.setAccessType(AccessType.valueOf(eventCreationDto.getAccessType().toUpperCase()));
        Location location = new Location();
        location.setId(eventCreationDto.getLocationId());
        event.setAddress(location);
        return event;
    }
    private void nullHunter(Object object, String message) {
        if (Objects.isNull(object)) {
            throw new DataNotFoundException(message, ExceptionCode.NOT_FOUND);
        }
    }
    private List<UltraEventDto> getEventDtoList(List<Event> events, Function<Event, UltraEventDto> function) {
        List<UltraEventDto> ultraEventDtoList = new ArrayList<>();
        for (Event event : events) {
            ultraEventDtoList.add(function.apply(event));
        }
        return ultraEventDtoList;
    }
    private Function<Event, UltraEventDto> getMethod(EventDtoType type) {

        if (type != null) {
            switch (type) {
                case MINIMAL:
                    return this::getMinimalEventDto;
                case FOR_LIST:
                    return this::getForListDto;
                case DETAIL:
                    return this::getShowEventDto;
                case FULL:
                    return this::getFullEventDto;
            }
        }
        return this::getMinimalEventDto;
    }

}
