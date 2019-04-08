package com.softserve.actent.repository;

import com.softserve.actent.model.entity.EventUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface UserEventsFilterRepository extends JpaRepository<EventUser,Long>, JpaSpecificationExecutor<EventUser> {
}
