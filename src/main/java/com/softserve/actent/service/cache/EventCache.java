package com.softserve.actent.service.cache;

import com.softserve.actent.constant.NumberConstants;
import com.softserve.actent.model.entity.Event;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventCache {

    private static final SimpleLRUCache<Long, Event> eventStore =
            new SimpleLRUCache<>(NumberConstants.EVENT_CACHE_CAPACITY);

    public void save(Event event) {
        eventStore.put(event.getId(), event);
    }

    public void saveAll(List<Event> eventList) {
        eventList.forEach(this::save);
    }

    public Event get(Long key) {
        return eventStore.get(key);
    }

    public boolean contains(Long id) {
        return eventStore.containsKey(id);
    }

    public void delete(Long id) {
        eventStore.remove(id);
    }
}
