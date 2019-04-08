package com.softserve.actent.service.impl;

import com.softserve.actent.constant.ExceptionMessages;
import com.softserve.actent.exceptions.DataNotFoundException;
import com.softserve.actent.exceptions.codes.ExceptionCode;
import com.softserve.actent.model.entity.Location;
import com.softserve.actent.repository.CityRepository;
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
    private final CityRepository cityRepository;

    @Autowired
    public LocationServiceImpl(LocationRepository locationRepository, CityRepository cityRepository) {
        this.locationRepository = locationRepository;
        this.cityRepository = cityRepository;
    }

    @Transactional
    @Override
    public Location add(Location location) {
        Location newLocation = new Location();
        newLocation.setAddress(location.getAddress());

        if (cityRepository.existsById(location.getCity().getId())) {
            newLocation.setCity(location.getCity());
            return locationRepository.save(newLocation);
        } else {
            throw new DataNotFoundException(
                    ExceptionMessages.CITY_NOT_FOUND,
                    ExceptionCode.NOT_FOUND);
        }
    }

    @Transactional
    @Override
    public Location update(Location location, Long id) {

        if (locationRepository.existsById(id)) {
            Location dbLocation = locationRepository.getOne(id);
            dbLocation.setAddress(location.getAddress());
            return locationRepository.save(dbLocation);
        } else {
            throw new DataNotFoundException(
                    ExceptionMessages.LOCATION_NOT_FOUND,
                    ExceptionCode.NOT_FOUND);
        }
    }

    @Override
    public Location get(Long locationId) {
        return locationRepository.findById(locationId)
                .orElseThrow(() -> new DataNotFoundException(
                        ExceptionMessages.LOCATION_NOT_FOUND,
                        ExceptionCode.NOT_FOUND));
    }

    @Override
    public List<Location> getAll() {
        return locationRepository.findAll();
    }

    @Transactional
    @Override
    public void delete(Long locationId) {
        Optional<Location> location = locationRepository.findById(locationId);
        if ((location.isPresent())) {
            locationRepository.deleteById(locationId);
        } else {
            throw new DataNotFoundException(
                    ExceptionMessages.LOCATION_NOT_FOUND,
                    ExceptionCode.NOT_FOUND);
        }
    }

    @Override
    public List<Location> getAllByCityId(Long cityId) {
        return locationRepository.findAllByCity_Id(cityId);
    }
}
