package com.softserve.actent.model.dto.converter;

import com.softserve.actent.exceptions.DataNotFoundException;
import com.softserve.actent.exceptions.codes.ExceptionCode;
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
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
public class UltraEventConverter {

    private final ModelMapper modelMapper;

    @Autowired
    public UltraEventConverter(ModelMapper modelMapper) {

        this.modelMapper = modelMapper;
    }

    public UltraEventDto convertToDto(Event event, String type) {

        if (type != null && !type.isBlank()) {

            if (type.equals("minimal")) {

                return getMinimalEventDto(event);

            } else if (type.equals("ostap")) {

                return getOstapDto(event);

            } else if (type.equals("show")) {

                return getShowEventDto(event);

            } else if (type.equals("full")) {

                return getFullEventDto(event);
            }
        }

        return getMinimalEventDto(event);
    }
    public Event convertToEntity(EventCreationDto eventCreationDto) {

        return extractEvent(eventCreationDto);
    }

    public List<UltraEventDto> convertToDtoList(List<Event> events, String type) {

        List<UltraEventDto> ultraEventDtoList = new ArrayList<>();

        if (type != null && !type.isBlank()) {

            if (type.equals("minimal")) {

                for (int i = 0; i < events.size(); i++) {
                    ultraEventDtoList.add(getMinimalEventDto(events.get(i)));
                }
                return ultraEventDtoList;

            } else if (type.equals("ostap")) {

                for (int i = 0; i < events.size(); i++) {
                    ultraEventDtoList.add(getOstapDto(events.get(i)));
                }
                return ultraEventDtoList;

            } else if (type.equals("show")) {

                for (int i = 0; i < events.size(); i++) {
                    ultraEventDtoList.add(getShowEventDto(events.get(i)));
                }
                return ultraEventDtoList;

            } else if (type.equals("full")) {

                for (int i = 0; i < events.size(); i++) {
                    ultraEventDtoList.add(getFullEventDto(events.get(i)));
                }
                return ultraEventDtoList;

            }
        }

        return null;
    }



    private MinimalEventDto getMinimalEventDto(Event event) {

        return modelMapper.map(event, MinimalEventDto.class);
    }
    private EventOstapDto getOstapDto(Event event) {

        EventOstapDto eventOstapDto = modelMapper.map(event, EventOstapDto.class);
        eventOstapDto.setLocationForEventDto(getLocation(event.getAddress()));
        eventOstapDto.setCategoryForEventDto(getCategory(event.getCategory()));

        return eventOstapDto;
    }
    private ShowEventDto getShowEventDto(Event event) {

        ShowEventDto showEventDto = modelMapper.map(event, ShowEventDto.class);
        showEventDto.setLocationForEventDto(getLocation(event.getAddress()));
        showEventDto.setCategoryForEventDto(getCategory(event.getCategory()));
        showEventDto.setChatForEventDto(getChat(event.getChat()));
        showEventDto.setUserForEventDto(getCreator(event.getCreator()));

        return showEventDto;
    }
    private EventFullDto getFullEventDto(Event event) {

        EventFullDto eventFullDto = modelMapper.map(event, EventFullDto.class);
        eventFullDto.setLocationForEventDto(getLocation(event.getAddress()));
        eventFullDto.setCategoryForEventDto(getCategory(event.getCategory()));
        eventFullDto.setChatForEventDto(getChat(event.getChat()));
        eventFullDto.setUserForEventDto(getCreator(event.getCreator()));
        eventFullDto.setEquipments(getEquipmentsIfTheyAre(event));
        eventFullDto.setEventForEventUserDtoList(getEventsUsers(event));
        // todo: add tags
        // todo: add reviews

        return eventFullDto;
    }



    private LocationForEventDto getLocation(Location location) {

        return null;
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
    private List<EquipmentDto> getEquipmentsIfTheyAre(Event event) {

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

        List<EventUserForEventDto> eventUserForEvenDtos = new ArrayList<>(eventUserList.size());

        for (EventUser eventUser : eventUserList) {
            eventUserForEvenDtos.add(getEventUserForEventDto(eventUser));
        }

        return eventUserForEvenDtos;
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

}
