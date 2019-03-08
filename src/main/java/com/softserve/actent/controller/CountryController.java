package com.softserve.actent.controller;

import com.softserve.actent.model.dto.CountryDto;
import com.softserve.actent.model.dto.IdDto;
import com.softserve.actent.model.entity.Country;
import com.softserve.actent.service.CountryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class CountryController {

    private final CountryService countryService;

    private final ModelMapper modelMapper;

    @Autowired
    public CountryController(CountryService countryService, ModelMapper modelMapper) {
        this.countryService = countryService;
        this.modelMapper = modelMapper;
    }

    @GetMapping(value = "/countries/{id}")
    public ResponseEntity<CountryDto> get(@PathVariable Long id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Country country = countryService.get(id);
        if (country == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        CountryDto countryDto = modelMapper.map(country, CountryDto.class);
        return new ResponseEntity<>(countryDto, HttpStatus.OK);
    }

    @GetMapping(value = "/countries")
    public ResponseEntity<List<CountryDto>> getAll() {
        List<Country> countries = countryService.getAll();
        if (countries == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<CountryDto> countryDtos = countries.stream()
                .map(country -> modelMapper.map(country, CountryDto.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(countryDtos, HttpStatus.OK);
    }

    @PostMapping(value = "/countries")
    public ResponseEntity<CountryDto> add(@RequestBody CountryDto countryDto) {
        Country country = modelMapper.map(countryDto, Country.class);
        countryService.add(country);
        return new ResponseEntity<>(countryDto, HttpStatus.CREATED);
    }

    @PutMapping(value = "/countries/{id}")
    public ResponseEntity<CountryDto> update(@PathVariable Long id, @RequestBody CountryDto countryDto) {
        Country country = countryService.update(modelMapper.map(countryDto, Country.class), id);
        return new ResponseEntity<>(modelMapper.map(country, CountryDto.class), HttpStatus.OK);
    }

    @DeleteMapping(value = "/countries/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Country country = countryService.get(id);
        if (country == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        countryService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
