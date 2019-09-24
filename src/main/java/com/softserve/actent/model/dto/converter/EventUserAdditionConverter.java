package com.softserve.actent.model.dto.converter;

import com.softserve.actent.model.dto.eventUser.EventUserFilterDto;
import com.softserve.actent.model.entity.EventUser;
import com.softserve.actent.utils.modelmapper.IModelMapperConverter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventUserAdditionConverter implements IModelMapperConverter<EventUser, EventUserFilterDto> {

    private final ModelMapper modelMapper;

    @Autowired
    public EventUserAdditionConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public EventUser convertToEntity(EventUserFilterDto dto) {
        return modelMapper.map(dto, EventUser.class);
    }

    @Override
    public EventUserFilterDto convertToDto(EventUser entity) {
        EventUserFilterDto filterDto = modelMapper.map(entity, EventUserFilterDto.class);
        filterDto.setEventUserType(entity.getType());
        return filterDto;
    }
}