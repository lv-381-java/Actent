package com.softserve.actent.service.impl;

import com.softserve.actent.constant.ExceptionMessages;
import com.softserve.actent.exceptions.DataNotFoundException;
import com.softserve.actent.exceptions.codes.ExceptionCode;
import com.softserve.actent.model.entity.Chat;
import com.softserve.actent.model.entity.ChatType;
import com.softserve.actent.model.entity.Equipment;
import com.softserve.actent.model.entity.Event;
import com.softserve.actent.model.entity.EventUser;
import com.softserve.actent.model.entity.Tag;
import com.softserve.actent.repository.CategoryRepository;
import com.softserve.actent.repository.ChatRepository;
import com.softserve.actent.repository.EventRepository;
import com.softserve.actent.repository.ImageRepository;
import com.softserve.actent.repository.LocationRepository;
import com.softserve.actent.repository.UserRepository;
import com.softserve.actent.service.cache.EventCache;
import com.softserve.actent.service.EventService;
import com.softserve.actent.service.SubscribeService;
import com.softserve.actent.service.cache.EventCacheMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class EventServiceImpl implements EventService {

    private final EventCache eventCache;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;
    private final ChatRepository chatRepository;
    private final SubscribeService subscribeService;

    @Autowired
    public EventServiceImpl(EventCache eventCache,
                            EventRepository eventRepository,
                            UserRepository userRepository,
                            LocationRepository locationRepository,
                            CategoryRepository categoryRepository,
                            ImageRepository imageRepository,
                            ChatRepository chatRepository,
                            SubscribeService subscribeService) {
        this.eventCache = eventCache;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.locationRepository = locationRepository;
        this.categoryRepository = categoryRepository;
        this.imageRepository = imageRepository;
        this.chatRepository = chatRepository;
        this.subscribeService = subscribeService;
    }

    @Override
    public Event add(Event event) {
        checkEvent(event);
        return getSavedEvent(event);
    }

    @Override
    public Event get(Long id) {

        if (eventCache.contains(id)) {
            return eventCache.get(id);
        }

        checkIfExist(id);
        Event event = eventRepository.getOne(id);
        eventCache.save(event);
        return event;
    }

    @Override
    public List<Event> getAll() {
        return eventRepository.findAll();
    }

    @Override
    public Page<Event> findActiveEvents(Pageable pageable) {
        return eventRepository.findByStartDateIsGreaterThanEqual(LocalDateTime.now(), pageable);
    }

    @Override
    public List<Event> getByTitle(String title) {
        List<Event> events = eventRepository.findByTitle(title);
        nullHunter(events, ExceptionMessages.EVENT_BY_THIS_TITLE_IS_NOT_FOUND);
        return events;
    }

    @Override
    public Event update(Event event, Long id) {

        nullHunter(event, ExceptionMessages.EVENT_CAN_NOT_BE_NULL);
        nullHunter(id, ExceptionMessages.ID_CAN_NOT_BE_NULL);
        Event preparedEvent = getPreparedEventFromDataBase(event, id);
        return getUpdatedEvent(preparedEvent);
    }

    @Override
    @Transactional
    public void delete(Long id) {

        checkIfExist(id);
        eventCache.cacheRefresh(id, EventCacheMethod.EVENT);
        eventRepository.deleteById(id);
    }

    @Transactional
    protected Event getSavedEvent(Event event) {

        event.setChat(createChat());
        event=eventRepository.save(event);
        subscribeService.checkSubscribers(event);
        return event;
    }

    @Transactional
    protected Event getUpdatedEvent(Event event) {

        return eventRepository.save(event);
    }

    private Chat createChat() {

        Chat chat = new Chat();
        chat.setType(ChatType.EVENT);
        return chatRepository.save(chat);
    }

    private Event getPreparedEventFromDataBase(Event event, Long id) {

        Event eventFromBase = null;
        Optional<Event> optionalEvent = eventRepository.findById(id);

        if (optionalEvent.isPresent()) {

            eventFromBase = optionalEvent.get();
            addFeedbackIfExist(event, eventFromBase);
            setChatIfExist(event, eventFromBase);
            setImageIfExist(event, eventFromBase);
            setNewCategoryIfExist(event, eventFromBase);
            setNewLocationIfExist(event, eventFromBase);
            addNewCreatorIfExist(event, eventFromBase);
            addEventUserIfExist(event, eventFromBase);
            addNewTagsIfExist(event, eventFromBase);
            addNewEquipmentsIfExist(event, eventFromBase);
        }

        event.setCreationDate(eventFromBase != null ? eventFromBase.getCreationDate() : null);
        event.setAccessType(eventFromBase != null ? eventFromBase.getAccessType() : null);
        event.setId(id);
        eventCache.cacheRefresh(id, EventCacheMethod.EVENT);
        return event;
    }

    private void addFeedbackIfExist(Event event, Event eventFromBase) {

        if (event.getFeedback() == null) {
            event.setFeedback(eventFromBase.getFeedback());
        } else {
            event.getFeedback().addAll(eventFromBase.getFeedback());
        }
    }

    private void setChatIfExist(Event event, Event eventFromBase) {

        if (event.getChat() == null) {
            event.setChat(eventFromBase.getChat());
        } else {
            isChatExist(event.getChat().getId());
        }
    }

    private void setImageIfExist(Event event, Event eventFromBase) {

        if (event.getImage() == null) {
            event.setImage(eventFromBase.getImage());
        } else {
            isImageExist(event.getImage().getId());
        }
    }

    private void setNewCategoryIfExist(Event event, Event eventFromBase) {

        if (event.getCategory() == null) {
            event.setCategory(eventFromBase.getCategory());
        } else {
            isCategoryExist(event.getCategory().getId());
        }
    }

    private void setNewLocationIfExist(Event event, Event eventFromBase) {

        if (event.getAddress() == null) {
            event.setAddress(eventFromBase.getAddress());
        } else {
            isLocationExist(event.getAddress().getId());
        }
    }

    private void addNewCreatorIfExist(Event event, Event eventFromBase) {

        if (event.getCreator() == null) {
            event.setCreator(eventFromBase.getCreator());
        } else {
            isUserExist(event.getCreator().getId());
        }
    }

    private void addEventUserIfExist(Event event, Event eventFromBase) {

        if (event.getEventUserList() == null) {
            event.setEventUserList(eventFromBase.getEventUserList());
        } else {

            List<EventUser> eventUserList = event.getEventUserList();
            List<EventUser> eventUserListFromBase = eventFromBase.getEventUserList();

            if (eventUserListFromBase != null) {
                for (EventUser eventUser : eventUserList) {
                    if (!eventUserListFromBase.contains(eventUser)) {
                        eventUserListFromBase.add(eventUser);
                    }
                }
                event.setEventUserList(eventUserListFromBase);
            }
        }
    }

    private void addNewTagsIfExist(Event event, Event eventFromBase) {

        if (event.getTags() == null) {
            event.setTags(eventFromBase.getTags());
        } else {

            List<Tag> tags = event.getTags();
            List<Tag> tagsFromBase = eventFromBase.getTags();

            if (tagsFromBase != null) {
                for (Tag tag : tags) {
                    if (!tagsFromBase.contains(tag)) {
                        tagsFromBase.add(tag);
                    }
                }
                event.setTags(tagsFromBase);
            }
        }
    }

    private void addNewEquipmentsIfExist(Event event, Event eventFromBase) {

        if (event.getEquipments() == null) {
            event.setEquipments(eventFromBase.getEquipments());
        } else {

            List<Equipment> equipments = event.getEquipments();
            List<Equipment> equipmentsFromBase = eventFromBase.getEquipments();

            if (equipmentsFromBase != null) {
                for (Equipment equipment : equipments) {
                    if (!equipmentsFromBase.contains(equipment)) {
                        equipmentsFromBase.add(equipment);
                    }
                }
                event.setEquipments(equipmentsFromBase);
            }
        }
    }

    private void checkIfExist(Long id) {

        if (!eventRepository.existsById(id)) {
            throw new DataNotFoundException(
                    ExceptionMessages.EVENT_BY_THIS_ID_IS_NOT_FOUND,
                    ExceptionCode.NOT_FOUND);
        }
    }

    private void checkEvent(Event event) {

        checkEventAndEventFieldsForNull(event);
        checkForExistenceResource(event);
    }

    private void checkEventAndEventFieldsForNull(Event event) {

        nullHunter(event, ExceptionMessages.EVENT_CAN_NOT_BE_NULL);
        nullHunter(event.getCategory(), ExceptionMessages.EVENT_CREATOR_CAN_NOT_BE_NULL);
        nullHunter(event.getAddress(), ExceptionMessages.EVENT_ADDRESS_CAN_NOT_BE_NULL);
        nullHunter(event.getCategory(), ExceptionMessages.EVENT_CATEGORY_CAN_NOT_BE_NULL);
        nullHunter(event.getAccessType(), ExceptionMessages.EVENT_ACCESS_TYPE_CAN_NOT_BE_NULL);
    }

    private void checkForExistenceResource(Event event) {

        isUserExist(event.getCreator().getId());
        isLocationExist(event.getAddress().getId());
        isCategoryExist(event.getCategory().getId());
    }

    private void nullHunter(Object object, String message) {
        if (Objects.isNull(object)) {
            throw new DataNotFoundException(message, ExceptionCode.NOT_FOUND);
        }
    }

    private void isUserExist(Long id) {
        if (!userRepository.existsById(id)) {
            throw new DataNotFoundException(ExceptionMessages.USER_BY_THIS_ID_IS_NOT_FOUND, ExceptionCode.NOT_FOUND);
        }
    }

    private void isLocationExist(Long id) {
        if (!locationRepository.existsById(id)) {
            throw new DataNotFoundException(ExceptionMessages.LOCATION_NOT_FOUND, ExceptionCode.NOT_FOUND);
        }
    }

    private void isCategoryExist(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new DataNotFoundException(ExceptionMessages.CATEGORY_IS_NOT_FOUND, ExceptionCode.NOT_FOUND);
        }
    }

    private void isChatExist(Long id) {
        if (!chatRepository.existsById(id)) {
            throw new DataNotFoundException(ExceptionMessages.CHAT_BY_THIS_ID_IS_NOT_FOUND, ExceptionCode.NOT_FOUND);
        }
    }

    private void isImageExist(Long id) {
        if (!imageRepository.existsById(id)) {
            throw new DataNotFoundException(ExceptionMessages.IMAGE_NOT_FOUND_WITH_ID, ExceptionCode.NOT_FOUND);
        }
    }

}
