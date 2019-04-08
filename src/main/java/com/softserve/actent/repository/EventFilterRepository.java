package com.softserve.actent.repository;

import com.softserve.actent.model.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface EventFilterRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {

}

