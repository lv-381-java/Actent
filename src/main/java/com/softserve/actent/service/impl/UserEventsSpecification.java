package com.softserve.actent.service.impl;

import com.softserve.actent.model.entity.EventUser;
import com.softserve.actent.model.entity.EventUserType;
import org.springframework.data.jpa.domain.Specification;

public class UserEventsSpecification {
    private static final String EVENT = "event";
    private static final String USER = "user";
    private static final String USER_ID = "id";
    private static final String START_DATE = "startDate";
    private static final String TYPE = "type";
    private static final String CATEGORY = "category";
    private static final String NAME = "name";
    private static final String CITY = "city";
    private static final String ADDRESS = "address";


    public static Specification<EventUser> getUserId(Long userId) {
        return (Specification<EventUser>) (root, query, criteriaBuilder) -> criteriaBuilder
                .equal(root.get(USER).get(USER_ID), userId);
    }

    public static Specification<EventUser> getPastEvents() {
        return (Specification<EventUser>) (root, query, criteriaBuilder) -> criteriaBuilder
                .lessThan(root.get(EVENT).get(START_DATE), criteriaBuilder.currentDate());
    }

    public static Specification<EventUser> getFutureEvents() {
        return (Specification<EventUser>) (root, query, criteriaBuilder) -> criteriaBuilder
                .greaterThan(root.get(EVENT).get(START_DATE), criteriaBuilder.currentDate());
    }

    public static Specification<EventUser> getUserIdAndPastEvents(Long userId) {
        return Specification.where(getUserId(userId))
                .and(getPastEvents());
    }

    public static Specification<EventUser> getUserIdAndFutureEvents(Long userId) {
        return Specification.where(getUserId(userId))
                .and(getFutureEvents());
    }

    public static Specification<EventUser> getUserType(EventUserType type) {
        return (Specification<EventUser>) (root, query, criteriaBuilder) -> {
            if (type == null) {
                return null;
            } else {
                return criteriaBuilder.equal(root.get(TYPE), type);
            }
        };
    }

    public static Specification<EventUser> getCategory(String name) {
        return (Specification<EventUser>) (root, query, criteriaBuilder) -> {
            if (name == null) {
                return null;
            } else {
                return criteriaBuilder.equal(root.get(EVENT).get(CATEGORY).get(NAME), name);
            }
        };
    }

    public static Specification<EventUser> getCity(String name) {
        return (Specification<EventUser>) (root, query, criteriaBuilder) -> {
            if (name == null || name.isEmpty()) {
                return null;
            } else {
                return criteriaBuilder.equal(root.get(EVENT).get(ADDRESS).get(CITY).get(NAME), name);
            }
        };
    }
}