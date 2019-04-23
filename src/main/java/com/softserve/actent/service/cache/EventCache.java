package com.softserve.actent.service.cache;

import com.softserve.actent.constant.NumberConstants;
import com.softserve.actent.model.entity.Event;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

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

    // todo: Save wrapper !!!
    public void delete(Long id) {
        eventStore.remove(id);
    }

    public void cacheRefresh(Long id, EventCacheMethod method) {
        refresh(id, getFunction(method));
    }

    private void refresh(Long id, BiFunction<Long, Long, Boolean> function) {

        deleteAll(eventStore
                .keySet()
                .stream()
                .filter(e -> function.apply(e, id))
                .collect(Collectors.toList()));
    }

    private BiFunction<Long, Long, Boolean> getFunction(EventCacheMethod method) {
        switch (method) {
            case CREATOR: return this::getCreator;
            case CATEGORY: return this::getCategory;
            case LOCATION: return this::getLocation;
            case IMAGE: return this::getImage;
            default: return null;
        }
    }

    private void deleteAll(List<Long> keys) {
        keys.forEach(eventStore::remove);
    }

    private boolean getCreator(Long storedId, Long changedId) {
        return eventStore.get(storedId).getCreator().getId().equals(changedId);
    }

    private boolean getCategory(Long storedId, Long changedId) {
        return eventStore.get(storedId).getCategory().getId().equals(changedId);
    }

    private boolean getLocation(Long storedId, Long changedId) {
        return eventStore.get(storedId).getAddress().getId().equals(changedId);
    }

    private boolean getImage(Long storedId, Long changedId) {
        return eventStore.get(storedId).getImage().getId().equals(changedId);
    }

}
