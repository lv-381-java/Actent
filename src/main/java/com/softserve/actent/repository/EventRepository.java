package com.softserve.actent.repository;

import com.softserve.actent.model.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByTitle(String title);

    List<Event>findByStartDateIsGreaterThanEqual(LocalDateTime localDateTime);
}
