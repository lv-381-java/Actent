package com.softserve.actent.service;

import com.softserve.actent.model.entity.EventUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EventUserService extends BaseCrudService<EventUser> {

    List<EventUser> getByEventId(Long id);

    List<EventUser> getByUserId(Long id);

    List<EventUser> getAllAssignedEventsForThisTime(Long userId, String startDate, String endDate);

    Page<EventUser> getAllByUserId(Long userId, Pageable pageable);

    Page<EventUser> getAllByUserIdPastEvents(Long userId, Pageable pageable);

    Page<EventUser> getAllByUserIdFutureEvents(Long userId, Pageable pageable);
}
