package com.softserve.actent.repository;

import com.softserve.actent.model.entity.Event;
import com.softserve.actent.model.entity.EventUser;
import com.softserve.actent.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventUserRepository extends JpaRepository<EventUser, Long> {

    List<EventUser> findByEvent(Event event);

    List<EventUser> findByUser(User user);

    Page<EventUser> findAllByUser_Id(Long userId, Pageable pageable);

    @Query(value = "SELECT * FROM events_users eu" +
            " JOIN events ON events.id = eu.event_id" +
            " WHERE eu.user_id = :userId" +
            "  and events.start_date < current_date", nativeQuery = true)
    Page<EventUser> findAllByUser_IdAndPastEvents(Long userId, Pageable pageable);

    @Query(value = "SELECT * FROM events_users eu" +
            " JOIN events ON events.id = eu.event_id" +
            " WHERE eu.user_id = :userId" +
            "  and events.start_date > current_date", nativeQuery = true)
    Page<EventUser> findAllByUser_IdAndFutureEvents(Long userId, Pageable pageable);

    @Query(value = "SELECT * FROM events_users eu" +
            " JOIN events ON events.id = eu.event_id" +
            " WHERE eu.user_id = :userId" +
            " AND events.start_date BETWEEN :startDate" +
            " AND :endDate", nativeQuery = true)
    List<EventUser> findAllAssignedEventsForThisTime(@Param("userId") Long userId, @Param("startDate") String startDate, @Param("endDate") String endDate);
}
