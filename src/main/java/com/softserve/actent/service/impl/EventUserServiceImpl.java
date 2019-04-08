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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;
import java.util.function.Predicate;

@Service
public class EventUserServiceImpl implements EventUserService {

    private final EventUserRepository eventUserRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Autowired
    public EventUserServiceImpl(EventUserRepository eventUserRepository,
                                EventRepository eventRepository,
                                UserRepository userRepository) {

        this.eventUserRepository = eventUserRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    @Override
    public EventUser add(EventUser entity) {

        checkForCorrectAddedData(entity);
        checkIfThisUserIsAssignedAlready(entity);
        return softSave(entity);
    }

    @Override
    public EventUser update(EventUser entity, Long id) {

        checkIfEventUserIsExist(id);
        checkForCorrectAddedData(entity);
        entity.setId(id);
        return softSave(entity);
    }

    @Transactional
    protected EventUser softSave(EventUser eventUser) {
        return eventUserRepository.save(eventUser);
    }

    @Override
    public EventUser get(Long id) {

        return eventUserRepository.findById(id).orElseThrow(() ->
                new DataNotFoundException(ExceptionMessages.EVENT_BY_THIS_ID_IS_NOT_FOUND, ExceptionCode.NOT_FOUND));
    }

    @Override
    public List<EventUser> getAll() {

        return eventUserRepository.findAll();
    }

    @Override
    public void delete(Long id) {

        checkIfEventUserIsExist(id);
        eventUserRepository.deleteById(id);
    }

    @Override
    public List<EventUser> getByEventId(Long id) {

        checkEventExistence(id);
        Event event = new Event();
        event.setId(id);

        return eventUserRepository.findByEvent(event);
    }

    @Override
    public List<EventUser> getByUserId(Long id) {

        checkUserExistence(id);
        User user = new User();
        user.setId(id);

        return eventUserRepository.findByUser(user);
    }

    private void checkForCorrectAddedData(EventUser eventUser) {

        checkEventUserForAndFieldsForNull(eventUser);
        checkEventExistence(eventUser.getEvent().getId());
        checkUserExistence(eventUser.getUser().getId());
    }

    private void checkEventUserForAndFieldsForNull(EventUser eventUser) {

        if (eventUser == null) {
            throw new DataNotFoundException(ExceptionMessages.EVENT_BY_THIS_ID_IS_NOT_FOUND, ExceptionCode.NOT_FOUND);
        }

        if (eventUser.getEvent() == null) {
            throw new DataNotFoundException(ExceptionMessages.EVENT_CAN_NOT_BE_NULL, ExceptionCode.THE_FIELD_IS_NULL);
        }

        if (eventUser.getUser() == null) {
            throw new DataNotFoundException(ExceptionMessages.USER_CAN_NOT_BE_NULL, ExceptionCode.THE_FIELD_IS_NULL);
        }

        if (eventUser.getType() == null) {
            throw new DataNotFoundException(ExceptionMessages.EVENT_ACCESS_TYPE_CAN_NOT_BE_NULL, ExceptionCode.THE_FIELD_IS_NULL);
        }
    }

    private void checkEventExistence(Long id) {

        if (!eventRepository.existsById(id)) {
            throw new DataNotFoundException(ExceptionMessages.EVENT_BY_THIS_ID_IS_NOT_FOUND, ExceptionCode.NOT_FOUND);
        }
    }

    private void checkUserExistence(Long id) {

        if (!userRepository.existsById(id)) {
            throw new DataNotFoundException(ExceptionMessages.USER_BY_THIS_ID_IS_NOT_FOUND, ExceptionCode.NOT_FOUND);
        }
    }

    private void checkIfThisUserIsAssignedAlready(EventUser eventUser) {

        List<EventUser> list = eventUserRepository.findByUser(eventUser.getUser());
        Predicate<Long> isPresent = id -> id.equals(eventUser.getEvent().getId());

        if (list.stream().anyMatch(x -> isPresent.test(x.getEvent().getId()))) {
            throw new DuplicateValueException(ExceptionMessages.USER_CAN_NOT_ASSIGNED_TWICE, ExceptionCode.DUPLICATE_VALUE);
        }
    }

    private void checkIfEventUserIsExist(Long id) {

        if (!eventUserRepository.existsById(id)) {
            throw new DataNotFoundException(ExceptionMessages.EVENT_BY_THIS_ID_IS_NOT_FOUND, ExceptionCode.NOT_FOUND);
        }
    }
}
