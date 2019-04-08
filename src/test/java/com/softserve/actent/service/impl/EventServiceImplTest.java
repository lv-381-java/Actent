package com.softserve.actent.service.impl;

import com.softserve.actent.exceptions.DataNotFoundException;
import com.softserve.actent.model.entity.AccessType;
import com.softserve.actent.model.entity.Category;
import com.softserve.actent.model.entity.Chat;
import com.softserve.actent.model.entity.Equipment;
import com.softserve.actent.model.entity.Event;
import com.softserve.actent.model.entity.EventUser;
import com.softserve.actent.model.entity.Image;
import com.softserve.actent.model.entity.Location;
import com.softserve.actent.model.entity.Review;
import com.softserve.actent.model.entity.Tag;
import com.softserve.actent.model.entity.User;
import com.softserve.actent.repository.*;
import com.softserve.actent.service.EventService;
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
public class EventServiceImplTest {

    @Autowired EventService eventService;

    @MockBean EventRepository eventRepository;

    @Mock Event event;
    @Mock User user;
    @Mock Location location;
    @Mock Category category;
    @Mock Chat chat;
    @Mock Image image;
    @Mock List<Event> list;
    @Mock List<Review> reviews;
    @Mock List<EventUser> eventUserList;
    @Mock List<Tag> tags;
    @Mock List<Equipment> equipments;

    @MockBean ChatRepository chatRepository;
    @MockBean UserRepository userRepository;
    @MockBean LocationRepository locationRepository;
    @MockBean CategoryRepository categoryRepository;

    private final Long id = 1L;
    private final Long wrongId = 111L;
    private final String title = "Title";
    private final int firstElement = 0;

    @Before
    public void setUp() {

        when(list.get(firstElement)).thenReturn(event);

        when(user.getId()).thenReturn(id);
        when(location.getId()).thenReturn(id);
        when(category.getId()).thenReturn(id);

        when(event.getId()).thenReturn(id);
        when(event.getAccessType()).thenReturn(AccessType.PUBLIC);
        when(event.getTitle()).thenReturn(title);
        when(event.getCreator()).thenReturn(user);
        when(event.getAddress()).thenReturn(location);
        when(event.getCategory()).thenReturn(category);

        when(chatRepository.existsById(id)).thenReturn(true);
        when(userRepository.existsById(id)).thenReturn(true);
        when(locationRepository.existsById(id)).thenReturn(true);
        when(categoryRepository.existsById(id)).thenReturn(true);

        when(chatRepository.findById(id)).thenReturn(Optional.of(chat));
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(locationRepository.findById(id)).thenReturn(Optional.of(location));
        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));

        when(eventRepository.existsById(id)).thenReturn(true);
        when(eventRepository.existsById(wrongId)).thenReturn(false);

        when(eventRepository.findById(id)).thenReturn(Optional.of(event));
        when(eventRepository.findById(wrongId)).thenReturn(Optional.empty());
        when(eventRepository.findByTitle(title)).thenReturn(list);

        when(eventRepository.findAll()).thenReturn(list);
        when(eventRepository.save(event)).thenReturn(event);
    }

    @Test
    public void getByIdTest() {

        assertThat(eventService.get(id).getAccessType()).isEqualTo(AccessType.PUBLIC);
    }

    @Test(expected = DataNotFoundException.class)
    public void getNullTest() {

        eventService.get(null);
    }

    @Test(expected = DataNotFoundException.class)
    public void getByIdWithExceptionTest() {

        eventService.get(wrongId);
    }

    @Test
    public void getAllTest() {

        assertThat(eventService.getAll().size()).isEqualTo(list.size());
        assertThat(eventService.getAll().get(firstElement).getTitle()).isEqualTo(list.get(firstElement).getTitle());
    }

    @Test
    public void getByTitleTest() {

        assertThat(eventService.getByTitle(title)).isEqualTo(list);
    }

    @Test
    public void addTest() {

        assertThat(eventService.add(event)).isEqualTo(event);
    }

    @Test
    public void updateTest() {

        when(event.getCreator()).thenReturn(null);
        assertThat(eventService.update(event, event.getId()).getTitle()).isEqualTo(event.getTitle());
    }

    @Test
    public void deleteByExistenceSourceTest() {

        eventService.delete(id);
        verify(eventRepository).deleteById(id);
    }

    @Test(expected = DataNotFoundException.class)
    public void deleteByNotExistenceSourceTest() {

        eventService.delete(wrongId);
        verify(eventRepository).deleteById(wrongId);
    }

    @Test(expected = DataNotFoundException.class)
    public void eventNullAddTest() {

        eventService.add(null);
    }

    @Test(expected = DataNotFoundException.class)
    public void creatorNullAddTest() {

        when(event.getCreator()).thenReturn(null).thenReturn(user);
        eventService.add(event);
    }

    @Test(expected = DataNotFoundException.class)
    public void locationNullAddTest() {

        when(event.getAddress()).thenReturn(null).thenReturn(location);
        eventService.add(event);
    }

    @Test(expected = DataNotFoundException.class)
    public void categoryNullAddTest() {

        when(event.getCategory()).thenReturn(null).thenReturn(category);
        eventService.add(event);
    }

    @Test(expected = DataNotFoundException.class)
    public void deleteNullTest() {

        eventService.delete(null);
    }

    @Test(expected = DataNotFoundException.class)
    public void eventNullUpdateTest() {

        eventService.update(null, id);
    }

    @Test(expected = DataNotFoundException.class)
    public void idNullUpdateTest() {

        eventService.update(event, null);
    }

    @Test
    public void feedbackNullUpdateTest() {

        when(event.getCreator()).thenReturn(null);
        when(event.getFeedback()).thenReturn(null).thenReturn(reviews);
        assertThat(eventService.update(event, id).getFeedback()).isEqualTo(reviews);
    }

    @Test
    public void chatNullUpdateTest() {

        when(event.getCreator()).thenReturn(null);
        when(event.getChat()).thenReturn(null).thenReturn(chat);
        assertThat(eventService.update(event, id).getChat()).isEqualTo(chat);
    }

    @Test
    public void imageNullUpdateTest() {

        when(event.getCreator()).thenReturn(null);
        when(event.getImage()).thenReturn(null).thenReturn(image);
        assertThat(eventService.update(event, id).getImage()).isEqualTo(image);
    }

    @Test
    public void categoryNullUpdateTest() {

        when(event.getCreator()).thenReturn(null);
        when(event.getCategory()).thenReturn(null).thenReturn(category);
        assertThat(eventService.update(event, id).getCategory()).isEqualTo(category);
    }

    @Test
    public void locationNullUpdateTest() {

        when(event.getCreator()).thenReturn(null);
        when(event.getAddress()).thenReturn(null).thenReturn(location);
        assertThat(eventService.update(event, id).getAddress()).isEqualTo(location);
    }

    @Test(expected = DataNotFoundException.class)
    public void creatorNotNullUpdateTest() {

        when(event.getCreator()).thenReturn(user);
        eventService.update(event, id);
    }

    @Test
    public void eventUserNullUpdateTest() {

        when(event.getCreator()).thenReturn(null);
        when(event.getEventUserList()).thenReturn(null).thenReturn(eventUserList);
        assertThat(eventService.update(event, id).getEventUserList()).isEqualTo(eventUserList);
    }

    @Test
    public void tagNullUpdateTest() {

        when(event.getCreator()).thenReturn(null);
        when(event.getTags()).thenReturn(null).thenReturn(tags);
        assertThat(eventService.update(event, id).getTags()).isEqualTo(tags);
    }

    @Test
    public void equipmentNullUpdateTest() {

        when(event.getCreator()).thenReturn(null);
        when(event.getEquipments()).thenReturn(null).thenReturn(equipments);
        assertThat(eventService.update(event, id).getEquipments()).isEqualTo(equipments);
    }
}
