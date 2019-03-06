package com.softserve.actent.controller;

import com.softserve.actent.model.dto.CountryDto;
import com.softserve.actent.model.dto.IdDto;
import com.softserve.actent.model.entity.Country;
import com.softserve.actent.service.CountryService;
import com.softserve.actent.utils.converters.CountryConverter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/countries")
public class CountryController {

    private final CountryService countryService;

    private final CountryConverter countryConverter;

    private final ModelMapper modelMapper;

    @Autowired
    public CountryController(CountryService countryService, CountryConverter countryConverter, ModelMapper modelMapper) {
        this.countryService = countryService;
        this.countryConverter = countryConverter;
        this.modelMapper = modelMapper;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CountryDto> get(@PathVariable Long id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Country country = countryService.get(id);
        if (country == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(countryConverter.convertToDto(country), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<CountryDto>> getAll() {
        List<Country> countries = countryService.getAll();
        if (countries == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(countryConverter.convertToDto(countries), HttpStatus.OK);
    }

    @PostMapping(value = "/add")
    public ResponseEntity<IdDto> add(@RequestBody @Valid CountryDto countryDto) {
        Country country = countryConverter.convertToEntity(countryDto);
        countryService.add(country);
        return new ResponseEntity<>(new IdDto(country.getId()), HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<IdDto> update(@PathVariable Long id, @RequestBody @Valid CountryDto countryDto) {
        Country country = countryService.get(id);
        if (country == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        modelMapper.map(countryDto, country);
        countryService.add(country);
        return new ResponseEntity<>(new IdDto(country.getId()), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Country country = countryService.get(id);
        if (country == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        countryService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
