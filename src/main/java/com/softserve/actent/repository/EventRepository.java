package com.softserve.actent.repository;

import com.softserve.actent.model.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByTitle(String title);

    Page<Event> findByStartDateIsGreaterThanEqual(LocalDateTime localDateTime, Pageable pageable);
}
