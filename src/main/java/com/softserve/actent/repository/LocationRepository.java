package com.softserve.actent.repository;


import com.softserve.actent.model.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    Location findByAddress(String address);

    Boolean existsByAddress(String address);
}
