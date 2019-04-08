package com.softserve.actent.service.impl;

import com.softserve.actent.constant.ExceptionMessages;
import com.softserve.actent.exceptions.DataNotFoundException;
import com.softserve.actent.exceptions.codes.ExceptionCode;
import com.softserve.actent.model.entity.Country;
import com.softserve.actent.repository.CountryRepository;
import com.softserve.actent.service.CountryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;

    @Autowired
    public CountryServiceImpl(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Transactional
    @Override
    public Country add(Country country) {
        return countryRepository.save(country);
    }

    @Transactional
    @Override
    public Country update(Country country, Long countryId) {

        if (countryRepository.existsById(countryId)) {
            country.setId(countryId);
            return countryRepository.save(country);
        } else {
            throw new DataNotFoundException(
                    ExceptionMessages.COUNTRY_NOT_FOUND,
                    ExceptionCode.NOT_FOUND);
        }
    }

    @Override
    public Country get(Long countryId) {
        return countryRepository.findById(countryId)
                .orElseThrow(() -> new DataNotFoundException(
                        ExceptionMessages.COUNTRY_NOT_FOUND,
                        ExceptionCode.NOT_FOUND));
    }

    @Override
    public List<Country> getAll() {
        return countryRepository.findAll();
    }

    @Transactional
    @Override
    public void delete(Long countryId) {
        Optional<Country> country = countryRepository.findById(countryId);

        if (country.isPresent()) {
            countryRepository.deleteById(countryId);
        } else {
            throw new DataNotFoundException(
                    ExceptionMessages.COUNTRY_NOT_FOUND,
                    ExceptionCode.NOT_FOUND);
        }
    }
}
