package com.softserve.actent.repository;

import com.softserve.actent.model.entity.Event;
import com.softserve.actent.model.entity.EventUser;
import com.softserve.actent.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EventUserRepository extends JpaRepository<EventUser, Long> {

    List<EventUser> findByEvent(Event event);
    List<EventUser> findByUser(User user);
}
