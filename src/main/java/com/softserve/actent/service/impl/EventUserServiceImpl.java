package com.softserve.actent.service.impl;

import com.softserve.actent.constant.ExceptionMessages;
import com.softserve.actent.exceptions.DataNotFoundException;
import com.softserve.actent.exceptions.DuplicateValueException;
import com.softserve.actent.exceptions.codes.ExceptionCode;
import com.softserve.actent.model.entity.Event;
import com.softserve.actent.model.entity.EventUser;
import com.softserve.actent.model.entity.User;
import com.softserve.actent.repository.EventRepository;
import com.softserve.actent.repository.EventUserRepository;
import com.softserve.actent.repository.UserRepository;
import com.softserve.actent.service.EventUserService;
import com.softserve.actent.service.cache.EventCache;
import com.softserve.actent.service.cache.EventCacheMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

@Service
public class EventUserServiceImpl implements EventUserService {

    private final EventCache eventCache;
    private final EventUserRepository eventUserRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Autowired
    public EventUserServiceImpl(EventUserRepository eventUserRepository,
                                EventRepository eventRepository,
                                UserRepository userRepository,
                                EventCache eventCache) {
        this.eventUserRepository = eventUserRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.eventCache = eventCache;
    }

    @Override
    public EventUser add(EventUser entity) {
        checkForCorrectAddedData(entity);
        checkIfThisUserIsAssignedAlready(entity);
        return softSave(entity);
    }

    @Override
    public EventUser update(EventUser entity, Long id) {
        isEventUserExist(id);
        checkForCorrectAddedData(entity);
        entity.setId(id);
        eventCache.cacheRefresh(id, EventCacheMethod.EVENT_USER);
        return softSave(entity);
    }

    @Transactional
    protected EventUser softSave(EventUser eventUser) {
        return eventUserRepository.save(eventUser);
    }

    @Override
    public EventUser get(Long id) {
        return eventUserRepository.findById(id).orElseThrow(() ->
                new DataNotFoundException(ExceptionMessages.EVENT_BY_THIS_ID_IS_NOT_FOUND,
                        ExceptionCode.NOT_FOUND));
    }

    @Override
    public List<EventUser> getAll() {
        return eventUserRepository.findAll();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        isEventUserExist(id);
        eventCache.cacheRefresh(id, EventCacheMethod.EVENT_USER);
        eventUserRepository.deleteById(id);
    }

    @Override
    public List<EventUser> getByEventId(Long id) {
        isEventExist(id);
        Event event = new Event();
        event.setId(id);
        return eventUserRepository.findByEvent(event);
    }

    @Override
    public List<EventUser> getByUserId(Long id) {
        isUserExist(id);
        User user = new User();
        user.setId(id);
        return eventUserRepository.findByUser(user);
    }
    @Override
    public List<EventUser> getAllAssignedEventsForThisTime(Long userId, String startDate, String endDate) {
        return eventUserRepository.findAllAssignedEventsForThisTime(userId, startDate, endDate);
    }

    @Override
    public Page<EventUser> getAllByUserId(Long userId, Pageable pageable) {
        return eventUserRepository.findAllByUser_Id(userId, pageable);
    }

    @Override
    public Page<EventUser> getAllByUserIdPastEvents(Long userId, Pageable pageable) {
        return eventUserRepository.findAllByUser_IdAndPastEvents(userId,pageable);
    }

    @Override
    public Page<EventUser> getAllByUserIdFutureEvents(Long userId, Pageable pageable) {
        return eventUserRepository.findAllByUser_IdAndFutureEvents(userId,pageable);
    }

    private void checkForCorrectAddedData(EventUser eventUser) {
        checkEventUserForAndFieldsForNull(eventUser);
        isEventExist(eventUser.getEvent().getId());
        isUserExist(eventUser.getUser().getId());
    }

    private void checkEventUserForAndFieldsForNull(EventUser eventUser) {
        nullHunter(eventUser, ExceptionMessages.EVENT_BY_THIS_ID_IS_NOT_FOUND);
        nullHunter(eventUser.getEvent(), ExceptionMessages.EVENT_CAN_NOT_BE_NULL);
        nullHunter(eventUser.getUser(), ExceptionMessages.USER_CAN_NOT_BE_NULL);
        nullHunter(eventUser.getType(), ExceptionMessages.EVENT_ACCESS_TYPE_CAN_NOT_BE_NULL);
    }

    private void checkIfThisUserIsAssignedAlready(EventUser eventUser) {

        List<EventUser> list = eventUserRepository.findByUser(eventUser.getUser());
        Predicate<Long> isPresent = id -> id.equals(eventUser.getEvent().getId());

        if (list.stream().anyMatch(x -> isPresent.test(x.getEvent().getId()))) {
            throw new DuplicateValueException(ExceptionMessages.USER_CAN_NOT_ASSIGNED_TWICE, ExceptionCode.DUPLICATE_VALUE);
        }
    }

    private void nullHunter(Object object, String message) {
        if (Objects.isNull(object)) {
            throw new DataNotFoundException(message, ExceptionCode.NOT_FOUND);
        }
    }

    private void isEventExist(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new DataNotFoundException(ExceptionMessages.EVENT_BY_THIS_ID_IS_NOT_FOUND, ExceptionCode.NOT_FOUND);
        }
    }

    private void isUserExist(Long id) {
        if (!userRepository.existsById(id)) {
            throw new DataNotFoundException(ExceptionMessages.USER_BY_THIS_ID_IS_NOT_FOUND, ExceptionCode.NOT_FOUND);
        }
    }

    private void isEventUserExist(Long id) {
        if (!eventUserRepository.existsById(id)) {
            throw new DataNotFoundException(ExceptionMessages.EVENT_BY_THIS_ID_IS_NOT_FOUND, ExceptionCode.NOT_FOUND);
        }
    }
}
