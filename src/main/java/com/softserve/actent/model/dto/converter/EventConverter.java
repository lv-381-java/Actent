package com.softserve.actent.model.dto.converter;

import com.softserve.actent.model.dto.equipment.EquipmentDto;
import com.softserve.actent.model.dto.event.*;
import com.softserve.actent.model.dto.eventUser.EventUserForEventDto;
import com.softserve.actent.model.entity.Event;
import com.softserve.actent.model.entity.EventUser;
import com.softserve.actent.utils.modelmapper.IModelMapperConverter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventConverter implements IModelMapperConverter<Event, EventDto> {

    private final ModelMapper modelMapper;

    @Autowired
    public EventConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public Event convertToEntity(EventDto dto) {

        // NOP
        return null;
    }

    @Override
    public EventDto convertToDto(Event entity) {

        EventDto eventDto = modelMapper.map(entity, EventDto.class);

        eventDto.setLocationForEventDto(extractLocation(entity));
        eventDto.setUserForEventDto(getUser(entity));
        eventDto.setChatForEventDto(getChat(entity));
        eventDto.setEquipments(getEquipmentsIfTheyAre(entity));
        eventDto.setEventForEventUserDtos(getEventsUsers(entity));
        eventDto.setCategoryForEventDto(getCategoryForEventDto(entity));

        return eventDto;
    }

    private LocationForEventDto extractLocation(Event event) {

        CountryForEventDto countryForEventDto = modelMapper.map(event.getAddress().getCity().getRegion().getCountry(), CountryForEventDto.class);
        RegionForEventDto regionForEventDto = modelMapper.map(event.getAddress().getCity().getRegion(), RegionForEventDto.class);
        CityForEventDto cityForEventDto = modelMapper.map(event.getAddress().getCity(), CityForEventDto.class);
        LocationForEventDto locationForEventDto = modelMapper.map(event.getAddress(), LocationForEventDto.class);

        regionForEventDto.setCityForEventDto(cityForEventDto);
        countryForEventDto.setRegionForEventDto(regionForEventDto);
        locationForEventDto.setCountryForEventDto(countryForEventDto);

        return locationForEventDto;
    }

    private UserForEventDto getUser(Event event) {

        return modelMapper.map(event.getCreator(), UserForEventDto.class);
    }

    private ChatForEventDto getChat(Event event) {

        return modelMapper.map(event.getChat(), ChatForEventDto.class);
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

    private CategoryForEventDto getCategoryForEventDto(Event event) {

        return modelMapper.map(event.getCategory(), CategoryForEventDto.class);
    }
}
