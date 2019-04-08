package com.softserve.actent.service.impl;

import com.softserve.actent.exceptions.DataNotFoundException;
import com.softserve.actent.model.entity.Event;
import com.softserve.actent.model.entity.EventUser;
import com.softserve.actent.model.entity.EventUserType;
import com.softserve.actent.model.entity.User;
import com.softserve.actent.repository.EventRepository;
import com.softserve.actent.repository.EventUserRepository;
import com.softserve.actent.repository.UserRepository;
import com.softserve.actent.service.EventUserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EventUserServiceImplTest {

    @Autowired EventUserService eventUserService;

    @MockBean EventUserRepository eventUserRepository;
    @MockBean EventRepository eventRepository;
    @MockBean UserRepository userRepository;

    @Mock EventUser eventUser;
    @Mock Event event;
    @Mock User user;
    @Mock List<EventUser> list;

    private final Long id = 1L;
    private final int firstElement = 0;
    private final Long wrongId = 111L;

    @Before
    public void setUp() {

        when(eventUser.getType()).thenReturn(EventUserType.PARTICIPANT);
        when(eventUser.getEvent()).thenReturn(event);
        when(eventUser.getUser()).thenReturn(user);

        when(event.getId()).thenReturn(id);
        when(user.getId()).thenReturn(id);

        when(list.get(firstElement)).thenReturn(eventUser);

        when(eventRepository.existsById(id)).thenReturn(true);
        when(userRepository.existsById(id)).thenReturn(true);

        when(eventUserRepository.findById(id)).thenReturn(Optional.of(eventUser));
        when(eventUserRepository.findAll()).thenReturn(list);
        when(eventUserRepository.save(eventUser)).thenReturn(eventUser);
        when(eventUserRepository.existsById(id)).thenReturn(true);
        when(eventUserRepository.findById(wrongId)).thenReturn(Optional.empty());
        when(eventUserRepository.existsById(wrongId)).thenReturn(false);
    }

    @Test
    public void getByIdTest() {

        assertThat(eventUserService.get(id).getType()).isEqualTo(EventUserType.PARTICIPANT);
    }

    @Test(expected = DataNotFoundException.class)
    public void getByWrongIdTest() {

        eventUserService.get(wrongId);
    }

    @Test
    public void getAllTest() {

        assertThat(eventUserService.getAll().get(firstElement)).isEqualTo(eventUser);
    }

    @Test
    public void addTest() {

        assertThat(eventUserService.add(eventUser)).isEqualTo(eventUser);
    }

    @Test
    public void updateTest() {

        when(eventUser.getType()).thenReturn(EventUserType.SPECTATOR);
        assertThat(eventUserService.update(eventUser, id).getType()).isEqualTo(EventUserType.SPECTATOR);
    }

    @Test
    public void deleteTest() {

        eventUserService.delete(id);
        verify(eventUserRepository).deleteById(id);
    }

    @Test(expected = DataNotFoundException.class)
    public void deleteByWrongId() {

        eventUserService.delete(wrongId);
    }

    @Test(expected = DataNotFoundException.class)
    public void addNullTest() {

        eventUserService.add(null);
    }

    @Test(expected = DataNotFoundException.class)
    public void eventNullAddTest() {

        when(eventUser.getEvent()).thenReturn(null).thenReturn(event);
        eventUserService.add(eventUser);
    }

    @Test(expected = DataNotFoundException.class)
    public void userNullAddTest() {

        when(eventUser.getUser()).thenReturn(null).thenReturn(user);
        eventUserService.add(eventUser);
    }

    @Test(expected = DataNotFoundException.class)
    public void typeNullAddTest() {

        when(eventUser.getType()).thenReturn(null).thenReturn(EventUserType.PARTICIPANT);
        eventUserService.add(eventUser);
    }

    @Test(expected = DataNotFoundException.class)
    public void getByNullTest() {

        eventUserService.get(null);
    }

    @Test(expected = DataNotFoundException.class)
    public void deleteByNull() {

        eventUserService.delete(null);
    }
}
