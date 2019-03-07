package com.softserve.actent.service.impl;

import com.softserve.actent.model.entity.Location;
import com.softserve.actent.repository.LocationRepository;
import com.softserve.actent.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;

    @Autowired
    public LocationServiceImpl(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public Location add(Location location) {
        return locationRepository.save(location);
    }

    @Override
    public List<Location> getAll() {
        return locationRepository.findAll();
    }

    @Override
    public Location get(Long locationId) {
        return locationRepository.getOne(locationId);
    }

    @Override
    public Location update(Location location, Long id) {
        if (locationRepository.findById(id).isPresent()) {
            location.setId(id);
            return locationRepository.save(location);
        } else {
            // TODO: else throw exception
            return null;
        }
    }

    @Override
    public void delete(Long locationId) {
        locationRepository.deleteById(locationId);
    }

    @Override
    public List<Location> getByCityId(Long cityId) {
        return locationRepository.findByCityId(cityId);
    }
}