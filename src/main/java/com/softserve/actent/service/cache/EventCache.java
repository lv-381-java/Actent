package com.softserve.actent.service.cache;

import com.softserve.actent.constant.NumberConstants;
import com.softserve.actent.model.entity.Event;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@Service
public class EventCache {

    private static final SimpleLRUCache<Long, Event> eventStore =
            new SimpleLRUCache<>(NumberConstants.EVENT_CACHE_CAPACITY);

    public synchronized void save(Event event) {
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

    public void cacheRefresh(Long id, EventCacheMethod method) {
        refresh(id, getFunction(method));
    }

    private void refresh(Long id, BiFunction<Long, Long, Boolean> function) {
        List<Long> collect =
                new ArrayList<>(eventStore.keySet())
                        .stream()
                        .filter(k -> function.apply(k, id))
                        .collect(Collectors.toList());
        deleteAll(collect);
    }

    private BiFunction<Long, Long, Boolean> getFunction(EventCacheMethod method) {
        switch (method) {
            case EVENT: return this::getEvent;
            case CREATOR: return this::getCreator;
            case CATEGORY: return this::getCategory;
            case LOCATION: return this::getLocation;
            case IMAGE: return this::getImage;
            case CHAT: return this::getChat;
            case REVIEW: return this::getReview;
            case TAG: return this::getTag;
            default: return null;
        }
    }

    private synchronized void deleteAll(List<Long> keys) {
        keys.forEach(eventStore::remove);
    }

    private boolean getEvent(Long storedId, Long changedId) {
        return eventStore.get(storedId).getId().equals(changedId);
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

    private boolean getChat(Long storedId, Long changedId) {
        return eventStore.get(storedId).getChat().getId().equals(changedId);
    }

    private boolean getReview(Long storedId, Long changedId) {
        return eventStore.get(storedId)
                .getFeedback()
                .stream()
                .anyMatch(r -> r.getId().equals(changedId));
    }

    private boolean getTag(Long storedId, Long changedId) {
        return eventStore.get(storedId)
                .getTags()
                .stream()
                .anyMatch(t -> t.getId().equals(changedId));
    }
}
