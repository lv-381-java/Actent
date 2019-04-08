package com.softserve.actent.service;

import com.softserve.actent.model.entity.EventUser;
import java.util.List;

public interface EventUserService extends BaseCrudService<EventUser> {

    List<EventUser> getByEventId(Long id);
    List<EventUser> getByUserId(Long id);
}
