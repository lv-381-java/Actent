package com.softserve.actent.service;

import com.softserve.actent.model.entity.Equipment;

import java.util.List;

public interface EquipmentService extends BaseCrudService<Equipment>{

    List<Equipment> getByAssignedEventId(Long assignedEvent_id);
}
