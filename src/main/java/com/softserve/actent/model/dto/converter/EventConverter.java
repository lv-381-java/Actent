package com.softserve.actent.model.dto.converter;

import com.softserve.actent.model.dto.event.EventCreationDto;
import com.softserve.actent.model.entity.Equipment;
import com.softserve.actent.model.entity.Event;
import com.softserve.actent.model.entity.Location;
import com.softserve.actent.utils.modelmapper.IModelMapperConverter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EventConverter implements IModelMapperConverter<Event, EventCreationDto> {

    private final ModelMapper modelMapper;
    private final EquipmentConverter equipmentConverter;

    @Autowired
    public EventConverter(ModelMapper modelMapper, EquipmentConverter equipmentConverter) {
        this.modelMapper = modelMapper;
        this.equipmentConverter = equipmentConverter;
    }

    @Override
    public Event convertToEntity(EventCreationDto dto) {
        return extractEvent(dto);
    }

    @Override
    public EventCreationDto convertToDto(Event entity) {

        // NOP

        return null;
    }

    private Event extractEvent(EventCreationDto eventCreationDto) {

        Event event = modelMapper.map(eventCreationDto, Event.class);
        event.setAddress(extractLocation(eventCreationDto));
        event.setEquipments(extractEquipments(eventCreationDto));

        return event;
    }

    private Location extractLocation(EventCreationDto eventCreationDto) {
        return modelMapper.map(eventCreationDto.getLocationForEventDto(), Location.class);
    }

    private List<Equipment> extractEquipments(EventCreationDto eventCreationDto) {
        return equipmentConverter.convertToEntity(eventCreationDto.getEquipmentDtoList());
    }
}