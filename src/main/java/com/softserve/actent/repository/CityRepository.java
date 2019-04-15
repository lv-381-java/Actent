package com.softserve.actent.repository;

import com.softserve.actent.model.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {

    List<City> findAllByRegion_Id(Long regionId);
    City findByName(String name);
}
