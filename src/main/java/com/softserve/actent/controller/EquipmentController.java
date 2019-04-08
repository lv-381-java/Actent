package com.softserve.actent.controller;

import com.softserve.actent.constant.StringConstants;
import com.softserve.actent.model.dto.IdDto;
import com.softserve.actent.model.dto.equipment.EquipmentCreateDto;
import com.softserve.actent.model.dto.equipment.EquipmentDto;
import com.softserve.actent.model.entity.Equipment;
import com.softserve.actent.service.EquipmentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.stream.Collectors;

import static com.softserve.actent.constant.UrlConstants.API_V1;
import static com.softserve.actent.constant.UrlConstants.EQUIPMENTS;

@Validated
@RestController
@RequestMapping(API_V1 + EQUIPMENTS)
@PreAuthorize("permitAll()")
public class EquipmentController {

    private final EquipmentService equipmentService;

    private final ModelMapper modelMapper;

    @Autowired
    public EquipmentController(EquipmentService equipmentService, ModelMapper modelMapper) {
        this.equipmentService = equipmentService;
        this.modelMapper = modelMapper;
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<EquipmentDto> getAllEquipments() {

        List<Equipment> equipments = equipmentService.getAll();

        return equipments.stream()
                .map(equipment -> modelMapper.map(equipment, EquipmentDto.class))
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EquipmentDto getEquipmentById(@PathVariable
                                         @NotNull
                                         @Positive(message = StringConstants.EQUIPMENT_ID_SHOULD_BE_POSITIVE)
                                                 Long id) {

        Equipment equipment = equipmentService.get(id);
        return modelMapper.map(equipment, EquipmentDto.class);
    }

    @PostMapping()
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.CREATED)
    public IdDto addEquipment(@Validated @RequestBody EquipmentCreateDto equipmentCreateDto) {

        Equipment equipment = equipmentService.add(modelMapper.map(equipmentCreateDto, Equipment.class));
        return new IdDto(equipment.getId());
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEquipmentById(@PathVariable
                                    @NotNull
                                    @Positive(message = StringConstants.EQUIPMENT_ID_SHOULD_BE_POSITIVE)
                                            Long id) {

        equipmentService.delete(id);
    }

    @PutMapping(value = "/{id}")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.OK)
    public EquipmentCreateDto updateEquipmentById(@PathVariable
                                                  @NotNull
                                                  @Positive(message = StringConstants.EQUIPMENT_ID_SHOULD_BE_POSITIVE)
                                                          Long id,
                                                  @Validated @RequestBody EquipmentCreateDto equipmentCreateDto) {

        Equipment equipment = equipmentService.update(modelMapper.map(equipmentCreateDto, Equipment.class), id);
        return modelMapper.map(equipment, EquipmentCreateDto.class);
    }

    @GetMapping(value = "/event/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<EquipmentDto> getByEventId(@PathVariable
                                           @NotNull
                                           @Positive(message = StringConstants.ASSIGNED_EVENT_ID_MUST_BE_POSITIVE)
                                                   Long id) {

        List<Equipment> equipments = equipmentService.getByAssignedEventId(id);

        return equipments.stream()
                .map(equipment -> modelMapper.map(equipment, EquipmentDto.class))
                .collect(Collectors.toList());
    }
}
