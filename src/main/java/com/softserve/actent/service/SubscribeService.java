package com.softserve.actent.service;

import com.softserve.actent.model.entity.Event;
import com.softserve.actent.model.entity.Subscribe;

import java.util.List;

public interface SubscribeService {

    Subscribe add(Subscribe subscribe);

    void delete(Long id);

    List<Subscribe> getAll();

    void checkSubscribers(Event event);

    void checkSubscribers();
}
