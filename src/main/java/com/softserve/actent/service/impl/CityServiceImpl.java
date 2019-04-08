package com.softserve.actent.service.impl;

import com.softserve.actent.constant.ExceptionMessages;
import com.softserve.actent.exceptions.DataNotFoundException;
import com.softserve.actent.exceptions.codes.ExceptionCode;
import com.softserve.actent.model.entity.City;
import com.softserve.actent.repository.CityRepository;
import com.softserve.actent.repository.RegionRepository;
import com.softserve.actent.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;
    private final RegionRepository regionRepository;

    @Autowired
    public CityServiceImpl(CityRepository cityRepository, RegionRepository regionRepository) {
        this.cityRepository = cityRepository;
        this.regionRepository = regionRepository;
    }

    @Transactional
    @Override
    public City add(City city) {
        City newCity = new City();
        newCity.setName(city.getName());

        if (regionRepository.existsById(city.getRegion().getId())) {
            newCity.setRegion(city.getRegion());
            return cityRepository.save(newCity);
        } else {
            throw new DataNotFoundException(
                    ExceptionMessages.REGION_NOT_FOUND,
                    ExceptionCode.NOT_FOUND);
        }
    }

    @Transactional
    @Override
    public City update(City city, Long cityId) {
        if (cityRepository.existsById(cityId)) {
            City dbCity = cityRepository.getOne(cityId);
            dbCity.setName(city.getName());
            return cityRepository.save(dbCity);
        } else {
            throw new DataNotFoundException(
                    ExceptionMessages.CITY_NOT_FOUND,
                    ExceptionCode.NOT_FOUND);
        }
    }

    @Override
    public City get(Long cityId) {
        return cityRepository.findById(cityId)
                .orElseThrow(() -> new DataNotFoundException(
                        ExceptionMessages.CITY_NOT_FOUND,
                        ExceptionCode.NOT_FOUND));
    }

    @Override
    public List<City> getAll() {
        return cityRepository.findAll();
    }

    @Transactional
    @Override
    public void delete(Long cityId) {
        Optional<City> city = cityRepository.findById(cityId);

        if (city.isPresent()) {
            cityRepository.deleteById(cityId);
        } else {
            throw new DataNotFoundException(
                    ExceptionMessages.CITY_NOT_FOUND,
                    ExceptionCode.NOT_FOUND);
        }
    }

    @Override
    public List<City> getAllByRegionId(Long regionId) {
        return cityRepository.findAllByRegion_Id(regionId);
    }
}
