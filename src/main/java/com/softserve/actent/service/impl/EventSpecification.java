package com.softserve.actent.service.impl;

import com.softserve.actent.model.entity.Event;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;

public class EventSpecification {

    private static final String EVENT_TITLE = "title";
    private static final String EVENT_CATEGORY = "category";
    private static final String CATEGORY_ID = "id";
    private static final String EVENT_ADDRESS = "address";
    private static final String EVENT_CITY = "city";
    private static final String CITY_NAME = "name";
    private static final String EVENT_START_DATE = "startDate";
    private static final Long POSITIVE_NUMBER = 0L;


    public static Specification<Event> getTitle(String title) {
        return (Specification<Event>) (root, query, cb) ->
                (title == null || title.isEmpty()) ? null
                        : cb.like(root.get(EVENT_TITLE), "%" + title + "%");
    }

    public static Specification<Event> getCategory(List<Long> categoriesId) {
        return (Specification<Event>) (root, query, cb) ->
                (categoriesId.isEmpty()) ? null
                        : cb.isTrue(root.get(EVENT_CATEGORY).get(CATEGORY_ID).in(categoriesId));
    }

    public static Specification<Event> getCity(String city) {
        return (Specification<Event>) (root, query, cb) ->
                (city == null || city.isEmpty()) ? null
                        : cb.equal(root.get(EVENT_ADDRESS).get(EVENT_CITY).get(CITY_NAME), city);
    }

    public static Specification<Event> getDate(LocalDateTime dateFrom, LocalDateTime dateTo) {
        return (Specification<Event>) (root, query, cb) -> {
            if (dateFrom == null && dateTo == null ) {
                return null;
            } else if (dateTo == null && dateFrom.isAfter(LocalDateTime.now())) {
                return cb.greaterThanOrEqualTo(root.get(EVENT_START_DATE), dateFrom);
            } else if (dateFrom == null) {
                return cb.lessThanOrEqualTo(root.get(EVENT_START_DATE), dateTo);
            } else {
                return cb.between(root.get(EVENT_START_DATE), dateFrom, dateTo);
            }
        };
    }


}
