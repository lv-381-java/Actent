package com.softserve.actent.service.impl;

import com.softserve.actent.model.entity.Location;
import com.softserve.actent.repository.LocationRepository;
import com.softserve.actent.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;

    @Autowired
    public LocationServiceImpl(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Transactional
    @Override
    public Location add(Location location) {
        return locationRepository.save(location);
    }

    @Transactional
    @Override
    public Location update(Location location, Long id) {
        if (locationRepository.existsById(id)) {
            location.setId(id);
            return locationRepository.save(location);
        } else {
            // TODO: else throw exception
            return null;
        }
    }

    @Override
    public Location get(Long id) {
        Optional<Location> optionalLocation = locationRepository.findById(id);
        return optionalLocation.orElse(null);
    }

    @Override
    public List<Location> getAll() {
        return locationRepository.findAll();
    }

    @Transactional
    @Override
    public void delete(Long id) {
        locationRepository.deleteById(id);
    }

    @Override
    public List<Location> getByCityId(Long cityId) {
        return locationRepository.findAllByCityId(cityId);
    }
}
