package com.softserve.actent.service.impl;

import com.softserve.actent.exceptions.DataNotFoundException;
import com.softserve.actent.model.entity.Equipment;
import com.softserve.actent.model.entity.Event;
import com.softserve.actent.model.entity.User;
import com.softserve.actent.repository.EquipmentRepository;
import com.softserve.actent.repository.EventRepository;
import com.softserve.actent.repository.UserRepository;
import com.softserve.actent.service.EquipmentService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EquipmentServiceImplTest {

    @Autowired
    private EquipmentService equipmentService;

    @MockBean
    private EquipmentRepository equipmentRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private EventRepository eventRepository;

    private final Long existingId = 1L;
    private final Long secondExistingId = 2L;
    private final Long nonExistingId = -1L;
    private final String title = "title";
    private final String secondTitle = "secondTitle";
    private final String description = "description";
    private final Boolean satisfied = false;
    private final Integer equipmentCount = 2;

    private Equipment equipment;
    private Equipment equipmentWithNonExistUser;
    private Equipment equipmentWithNonExistEvent;
    private Equipment equipmentWithoutUser;
    private Event existingEvent;
    private Event nonExistingEvent;
    private User existingUser;
    private User nonExistingUser;

    List<Equipment> allEquipments;


    @Before
    public void setUp() {

        existingEvent = new Event();
        existingEvent.setId(existingId);
        existingUser = new User();
        existingUser.setId(existingId);

        nonExistingEvent = new Event();
        nonExistingEvent.setId(nonExistingId);
        nonExistingUser = new User();
        nonExistingUser.setId(nonExistingId);

        equipment = new Equipment(existingId, title, description, satisfied, existingUser, existingEvent);
        equipmentWithNonExistUser = new Equipment(secondExistingId, secondTitle, description, satisfied, nonExistingUser, existingEvent);
        equipmentWithNonExistEvent = new Equipment(secondExistingId, secondTitle, description, satisfied, existingUser, nonExistingEvent);
        equipmentWithoutUser = new Equipment(secondExistingId, secondTitle, description, satisfied, null, existingEvent);

        allEquipments = Arrays.asList(equipment, equipmentWithoutUser);

        Mockito.when(equipmentRepository.existsById(existingId)).thenReturn(true);
        Mockito.when(equipmentRepository.existsById(nonExistingId)).thenReturn(false);
        Mockito.when(equipmentRepository.findById(existingId)).thenReturn(Optional.of(equipment));
        Mockito.when(equipmentRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        Mockito.when(equipmentRepository.findAll()).thenReturn(allEquipments);

        Mockito.when(eventRepository.existsById(existingId)).thenReturn(true);
        Mockito.when(userRepository.existsById(existingId)).thenReturn(true);
        Mockito.when(eventRepository.existsById(nonExistingId)).thenReturn(false);
        Mockito.when(userRepository.existsById(nonExistingId)).thenReturn(false);

        Mockito.when(equipmentRepository.save(equipment)).thenReturn(equipment);
        Mockito.when(equipmentRepository.save(equipmentWithoutUser)).thenReturn(equipmentWithoutUser);

        Mockito.doNothing().when(equipmentRepository).deleteById(existingId);
        Mockito.doNothing().when(equipmentRepository).deleteById(nonExistingId);
    }

    @Test
    public void whenValidId_thenEquipmentShouldBeFound() {

        Equipment found = equipmentService.get(existingId);
        Mockito.verify(equipmentRepository, VerificationModeFactory.times(1)).findById(Mockito.anyLong());

        assertThat(found.getTitle()).isEqualTo(title);
    }

    @Test(expected = DataNotFoundException.class)
    public void whenInValidId_thenExceptionShouldBeThrown() {

        equipmentService.get(nonExistingId);
    }

    @Test
    public void whenGetAll_thenListOfEquipmentsShouldBeReturned() {

        List<Equipment> founds = equipmentService.getAll();
        Mockito.verify(equipmentRepository, VerificationModeFactory.times(1)).findAll();

        assertThat(founds.size()).isEqualTo(equipmentCount);
        assertThat(founds.get(0).getTitle()).isEqualTo(title);
        assertThat(founds.get(1).getTitle()).isEqualTo(secondTitle);
    }

    @Test
    public void whenAddEquipmentWithExistingUserAndEvent_thenEquipmentShouldBeReturned() {

        assertThat(equipmentService.add(equipment)).isEqualTo(equipment);
    }

    @Test(expected = DataNotFoundException.class)
    public void whenAddEquipmentWithNonExistingUserId_thenExceptionShouldBeThrown(){

        equipmentService.add(equipmentWithNonExistUser);
    }

    @Test(expected = DataNotFoundException.class)
    public void whenAddEquipmentWithNonExistingEventId_thenExceptionShouldBeThrown(){

        equipmentService.add(equipmentWithNonExistEvent);
    }

    @Test
    public void whenAddEquipmentWithoutUser_thenEquipmentWithoutUserShouldBeReturned() {

        assertThat(equipmentService.add(equipmentWithoutUser)).isEqualTo(equipmentWithoutUser);
    }

    @Test
    public void whenUpdateEquipmentWithExistingId_thenEquipmentShouldBeReturned(){

        assertThat(equipmentService.update(equipmentWithoutUser, existingId).getTitle()).isEqualTo(secondTitle);
    }

    @Test(expected = DataNotFoundException.class)
    public void whenUpdateEquipmentWithNonExistingId_thenExceptionShouldBeThrown(){

        equipmentService.update(equipmentWithoutUser, nonExistingId);
    }

    @Test
    public void whenDeleteTagWithExistingId_thenNothingShouldBeReturned() {

        equipmentService.delete(existingId);
    }

    @Test(expected = DataNotFoundException.class)
    public void whenDeleteTagWithNonExistingId_thenExceptionShouldBeThrown() {

        equipmentService.delete(nonExistingId);
    }

    @After
    public void tearDown() {

        equipment = null;
        equipmentWithoutUser = null;
        equipmentWithNonExistEvent = null;
        equipmentWithNonExistUser = null;
        allEquipments = null;
    }
}
