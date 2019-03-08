package com.softserve.actent.service.impl;

import com.softserve.actent.constant.ExceptionMessages;
import com.softserve.actent.exceptions.ResourceNotFoundException;
import com.softserve.actent.exceptions.codes.ExceptionCode;
import com.softserve.actent.model.entity.Category;
import com.softserve.actent.model.entity.Chat;
import com.softserve.actent.model.entity.ChatType;
import com.softserve.actent.model.entity.City;
import com.softserve.actent.model.entity.Equipment;
import com.softserve.actent.model.entity.Event;
import com.softserve.actent.model.entity.Image;
import com.softserve.actent.model.entity.Location;
import com.softserve.actent.model.entity.User;
import com.softserve.actent.repository.EventRepository;
import com.softserve.actent.service.CategoryService;
import com.softserve.actent.service.ChatService;
import com.softserve.actent.service.CityService;
import com.softserve.actent.service.EquipmentService;
import com.softserve.actent.service.EventService;
import com.softserve.actent.service.ImageService;
import com.softserve.actent.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final CityService cityService;
    private final UserService userService;
    private final ImageService imageService;
    private final EquipmentService equipmentService;
    private final CategoryService categoryService;
    private final ChatService chatService;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository,
                            CityService cityService,
                            UserService userService,
                            ImageService imageService,
                            EquipmentService equipmentService,
                            CategoryService categoryService,
                            ChatService chatService) {

        this.eventRepository = eventRepository;
        this.cityService = cityService;
        this.userService = userService;
        this.imageService = imageService;
        this.equipmentService = equipmentService;
        this.categoryService = categoryService;
        this.chatService = chatService;
    }

    @Override public Event add(Event event) {

        Event preparedEvent = getPreparedEventForSaveInDataBase(event);

        return getSavedEvent(preparedEvent);
    }

    @Override public Event get(Long id) {

        Optional<Event> optionalEvent = eventRepository.findById(id);

        return optionalEvent.orElseThrow(()
                -> new ResourceNotFoundException(
                ExceptionMessages.EVENT_BY_THIS_ID_IS_NOT_FOUND,
                ExceptionCode.NOT_FOUND));
    }

    @Override public List<Event> getAll() {

        return eventRepository.findAll();
    }

    @Override public Event getByTitle(String title) {

        Optional<Event> optionalEvent = eventRepository.findByTitle(title);

        return optionalEvent.orElseThrow(()
                -> new ResourceNotFoundException(
                ExceptionMessages.EVENT_BY_THIS_TITLE_IS_NOT_FOUND,
                ExceptionCode.NOT_FOUND));
    }

    @Override public Event update(Event event, Long id) {

        Event preparedEvent = getPreparedEventFromDataBase(event, id);

        return getSavedEvent(preparedEvent);
    }

    @Override
    @Transactional
    public void delete(Long id) {

        eventRepository.deleteById(id);
    }

    private Event getPreparedEventForSaveInDataBase(Event event) {

        event.setCreator(getUserFromDataBase(event));
        event.setAddress(getLocationFromDataBase(event));
        event.setCategory(getCategoryFromDataBase(event));
        event.setChat(createChat());
        event.setEquipments(saveEquipmentIfExist(event));
        event.setImage(saveImageIfExist(event));
        event.setParticipants(setCreatorLikeParticipant(event));
        event.setSpectators(setCreatorLikeSpectator(event));

        return event;
    }

    private User getUserFromDataBase(Event event) {

        return userService.get(event.getCreator().getId());
    }

    private Location getLocationFromDataBase(Event event) {

        Location location = new Location();
        City city = cityService.get(event.getAddress().getCity().getId());
        location.setCity(city);
        location.setAddress(event.getAddress().getAddress());

        return location;
    }

    private Chat createChat() {

        return chatService.addChat(ChatType.EVENT.toString());
    }

    private List<Equipment> saveEquipmentIfExist(Event event) {

        List<Equipment> equipments = null;

        if (event.getEquipments() != null) {

            equipments = event.getEquipments();

            for (int i = 0; i < equipments.size(); i++) {

                Equipment equipment = equipments.get(i);
                equipment = equipmentService.add(equipment);
                equipments.set(i, equipment);
            }
        }

        return equipments;
    }

    private Image saveImageIfExist(Event event) {

        Image image = null;

        if (event.getImage() != null) {

            image = imageService.add(event.getImage());
        }

        return image;
    }

    private List<User> setCreatorLikeParticipant(Event event) {

        List<User> participants = null;

        if (event.getParticipants() != null) {

            participants = event.getParticipants();
            participants.add(event.getCreator());
        }

        return participants;
    }

    private List<User> setCreatorLikeSpectator(Event event) {

        List<User> spectators = null;

        if (event.getSpectators() != null) {

            spectators = event.getSpectators();
            spectators.add(event.getCreator());
        }

        return spectators;
    }

    private Category getCategoryFromDataBase(Event event) {

        return categoryService.get(event.getCategory().getId());
    }

    private Event getPreparedEventFromDataBase(Event event, Long id) {

        if (!eventRepository.existsById(id)) {

            throw new ResourceNotFoundException(ExceptionMessages.EVENT_BY_THIS_ID_IS_NOT_FOUND, ExceptionCode.NOT_FOUND);
        }

        event.setId(id);

        return event;
    }

    @Transactional
    protected Event getSavedEvent(Event event) {

        return eventRepository.save(event);
    }
}
