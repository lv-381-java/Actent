package com.softserve.actent.model.dto.converter;

import com.softserve.actent.model.dto.event.EquipmentForEventDto;
import com.softserve.actent.model.entity.Equipment;
import com.softserve.actent.utils.modelmapper.IModelMapperConverter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EquipmentConverter implements IModelMapperConverter<Equipment, EquipmentForEventDto> {

    private final ModelMapper modelMapper;

    @Autowired
    public EquipmentConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public Equipment convertToEntity(EquipmentForEventDto dto) {
        return modelMapper.map(dto, Equipment.class);
    }

    @Override
    public EquipmentForEventDto convertToDto(Equipment entity) {
        return modelMapper.map(entity, EquipmentForEventDto.class);
    }
}