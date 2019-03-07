package com.softserve.actent.service.impl;

import com.softserve.actent.model.entity.City;
import com.softserve.actent.repository.CityRepository;
import com.softserve.actent.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;

    @Autowired
    public CityServiceImpl(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Override
    public City add(City city) {
        return cityRepository.save(city);
    }

    @Override
    public List<City> getAll() {
        return cityRepository.findAll();
    }

    @Override
    public City get(Long id) {
        return cityRepository.getOne(id);
    }

    @Override
    public City update(City city, Long id) {
        if (cityRepository.findById(id).isPresent()) {
            city.setId(id);
            return cityRepository.save(city);
        } else {
            // TODO: else throw exception
            return null;
        }
    }

    @Override
    public void delete(Long id) {
        cityRepository.deleteById(id);
    }

    @Override
    public List<City> getByRegionId(Long regionId) {
        return cityRepository.getAllByRegionId(regionId);
    }

    @Override
    public City getName(String cityName) {
        return cityRepository.findByName(cityName);
    }
}
