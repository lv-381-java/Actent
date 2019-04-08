package com.softserve.actent.service;

import com.softserve.actent.model.entity.Event;
import java.util.List;

public interface EventService extends BaseCrudService<Event> {

    List<Event> getByTitle(String title);

    List<Event> findActiveEvents();
}
