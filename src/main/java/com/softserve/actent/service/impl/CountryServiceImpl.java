package com.softserve.actent.service.impl;

import com.softserve.actent.model.entity.Country;
import com.softserve.actent.repository.CountryRepository;
import com.softserve.actent.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;

    @Autowired
    public CountryServiceImpl(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public Country get(Long countryId) {
        return countryRepository.getOne(countryId);
    }

    @Override
    public List<Country> getAll() {
        return countryRepository.findAll();
    }

    @Override
    public Country add(Country country) {
        return countryRepository.save(country);
    }

    @Override
    public void delete(Long countryId) {
        countryRepository.deleteById(countryId);
    }
}
