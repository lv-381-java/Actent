package com.softserve.actent.service.impl;

import com.softserve.actent.constant.ExceptionMessages;
import com.softserve.actent.exceptions.ResourceNotFoundException;
import com.softserve.actent.exceptions.codes.ExceptionCode;
import com.softserve.actent.model.entity.Equipment;
import com.softserve.actent.repository.EquipmentRepository;
import com.softserve.actent.service.EquipmentService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
public class EquipmentServiceImpl implements EquipmentService {

    private final EquipmentRepository equipmentRepository;

    @Autowired
    public EquipmentServiceImpl(EquipmentRepository equipmentRepository) {
        this.equipmentRepository = equipmentRepository;
    }

    @Transactional
    @Override
    public Equipment add(Equipment entity) {

        return equipmentRepository.save(entity);
        // TODO: throw exception
    }

    @Transactional
    @Override
    public Equipment update(Equipment entity, Long id) {
        if (equipmentRepository.findById(id).isPresent()) {
            entity.setId(id);
            return equipmentRepository.save(entity);
        } else {

            // TODO: else throw exception or so
            return null;
        }
    }

    @Override
    public Equipment get(Long id) {

        Optional<Equipment> optionalEquipment = equipmentRepository.findById(id);

        return optionalEquipment.orElseThrow(()
                -> new ResourceNotFoundException(
                ExceptionMessages.EQUIPMENT_BY_THIS_ID_IS_NOT_FOUND,
                ExceptionCode.NOT_FOUND
        ));
    }

    @Override
    public List<Equipment> getAll() {

        List<Equipment> equipmentList = equipmentRepository.findAll();

        if (equipmentList.isEmpty()) {

            throw new ResourceNotFoundException(
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

            throw new ResourceNotFoundException(
                    ExceptionMessages.EQUIPMENT_BY_THIS_ID_IS_NOT_FOUND,
                    ExceptionCode.NOT_FOUND
            );
        }
    }
}
