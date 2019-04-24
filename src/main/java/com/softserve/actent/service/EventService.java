package com.softserve.actent.service;

import com.softserve.actent.model.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EventService extends BaseCrudService<Event> {

    List<Event> getByTitle(String title);

    Page<Event> findActiveEvents(Pageable pageable);
}
