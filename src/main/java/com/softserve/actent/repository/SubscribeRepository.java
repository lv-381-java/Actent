package com.softserve.actent.repository;

import com.softserve.actent.model.entity.Subscribe;
import com.softserve.actent.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscribeRepository extends JpaRepository<Subscribe, Long> {

    List<Subscribe> findAllByCategoryAndCity(String Category, String City);

    List<Subscribe> findAllBySubscriber(User subscriber);
}
