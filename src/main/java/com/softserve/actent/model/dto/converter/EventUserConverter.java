package com.softserve.actent.model.dto.converter;

import com.softserve.actent.model.dto.eventUser.EventUserDto;
import com.softserve.actent.model.dto.eventUser.EventUserShowDto;
import com.softserve.actent.model.entity.EventUser;
import com.softserve.actent.model.entity.EventUserType;
import com.softserve.actent.utils.modelmapper.IModelMapperConverter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventUserConverter implements IModelMapperConverter<EventUser, EventUserDto> {

    private final ModelMapper modelMapper;

    @Autowired
    public EventUserConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public EventUser convertToEntity(EventUserDto dto) {
        EventUser eventUser = modelMapper.map(dto, EventUser.class);
        eventUser.setType(EventUserType.valueOf(dto.getEventUserType().toUpperCase()));
        return eventUser;
    }

    @Override
    public EventUserDto convertToDto(EventUser entity) {

        EventUserDto eventUserDto = modelMapper.map(entity, EventUserDto.class);
        eventUserDto.setEventUserType(entity.getType().name());
        return eventUserDto;
    }

    public List<EventUserShowDto> convertToListShowDto(List<EventUser> eventUserList) {
        List<EventUserShowDto> eventUserShowDtoList = null;
        if (eventUserList != null) {
            eventUserShowDtoList = eventUserList.stream().map(this::convertToShowDto).collect(Collectors.toList());
        }
        return eventUserShowDtoList;
    }

    private EventUserShowDto convertToShowDto(EventUser eventUser) {
        EventUserShowDto eventUserShowDto = modelMapper.map(eventUser, EventUserShowDto.class);
        eventUserShowDto.setEventUserType(eventUser.getType());
        return eventUserShowDto;
    }

}
