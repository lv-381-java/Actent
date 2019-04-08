package com.softserve.actent.service.impl;

import com.softserve.actent.constant.ExceptionMessages;
import com.softserve.actent.exceptions.DataNotFoundException;
import com.softserve.actent.exceptions.codes.ExceptionCode;
import com.softserve.actent.model.entity.Equipment;
import com.softserve.actent.repository.EquipmentRepository;
import com.softserve.actent.repository.EventRepository;
import com.softserve.actent.repository.UserRepository;
import com.softserve.actent.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class EquipmentServiceImpl implements EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Autowired
    public EquipmentServiceImpl(EquipmentRepository equipmentRepository, EventRepository eventRepository, UserRepository userRepository) {
        this.equipmentRepository = equipmentRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public Equipment add(Equipment entity) {

        if (entity.getAssignedEvent() == null) {

            throw new DataNotFoundException(
                    ExceptionMessages.ASSIGNED_EVENT_EMPTY,
                    ExceptionCode.NOT_FOUND
            );
        } else if (!eventRepository.existsById(entity.getAssignedEvent().getId())) {

            throw new DataNotFoundException(
                    ExceptionMessages.EVENT_BY_THIS_ID_IS_NOT_FOUND,
                    ExceptionCode.NOT_FOUND
            );
        } else if (entity.getAssignedUser() != null && !userRepository.existsById(entity.getAssignedUser().getId())) {

            throw new DataNotFoundException(
                    ExceptionMessages.USER_BY_THIS_ID_IS_NOT_FOUND,
                    ExceptionCode.NOT_FOUND
            );
        } else {

            return equipmentRepository.save(entity);
        }
    }

    @Transactional
    @Override
    public Equipment update(Equipment entity, Long id) {

        if (!equipmentRepository.existsById(id)) {

            throw new DataNotFoundException(
                    ExceptionMessages.EQUIPMENT_BY_THIS_ID_IS_NOT_FOUND,
                    ExceptionCode.NOT_FOUND
            );
        } else if (!eventRepository.existsById(entity.getAssignedEvent().getId())) {

            throw new DataNotFoundException(
                    ExceptionMessages.EVENT_BY_THIS_ID_IS_NOT_FOUND,
                    ExceptionCode.NOT_FOUND
            );
        } else if (entity.getAssignedUser() != null && !userRepository.existsById(entity.getAssignedUser().getId())) {

            throw new DataNotFoundException(
                    ExceptionMessages.USER_BY_THIS_ID_IS_NOT_FOUND,
                    ExceptionCode.NOT_FOUND
            );
        } else {

            entity.setId(id);
            return equipmentRepository.save(entity);
        }
    }

    @Override
    public Equipment get(Long id) {

        Optional<Equipment> optionalEquipment = equipmentRepository.findById(id);

        return optionalEquipment.orElseThrow(()
                -> new DataNotFoundException(
                ExceptionMessages.EQUIPMENT_BY_THIS_ID_IS_NOT_FOUND,
                ExceptionCode.NOT_FOUND
        ));
    }

    @Override
    public List<Equipment> getAll() {

        List<Equipment> equipmentList = equipmentRepository.findAll();

        if (equipmentList.isEmpty()) {

            throw new DataNotFoundException(
                    ExceptionMessages.EQUIPMENTS_ARE_NOT_FOUND,
                    ExceptionCode.NOT_FOUND
            );
        } else {

            return equipmentList;
        }
    }

    @Transactional
    @Override
    public void delete(Long id) {

        Optional<Equipment> optionalEquipment = equipmentRepository.findById(id);

        if (optionalEquipment.isPresent()) {

            equipmentRepository.deleteById(id);
        } else {

            throw new DataNotFoundException(
                    ExceptionMessages.EQUIPMENT_BY_THIS_ID_IS_NOT_FOUND,
                    ExceptionCode.NOT_FOUND
            );
        }
    }

    @Override
    public List<Equipment> getByAssignedEventId(Long assignedEvent_id) {

        List<Equipment> equipments = equipmentRepository.findAllByAssignedEventId(assignedEvent_id);
        if (equipments.isEmpty()) {

            throw new DataNotFoundException(
                    ExceptionMessages.EQUIPMENTS_ARE_NOT_FOUND,
                    ExceptionCode.NOT_FOUND
            );
        } else {

            return equipments;
        }
    }
}
