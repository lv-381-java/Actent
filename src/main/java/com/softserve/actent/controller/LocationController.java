package com.softserve.actent.controller;

import com.softserve.actent.model.dto.IdDto;
import com.softserve.actent.model.dto.LocationDto;
import com.softserve.actent.model.entity.Location;
import com.softserve.actent.service.CityService;
import com.softserve.actent.service.LocationService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class LocationController {

    private final LocationService locationService;

    private final CityService cityService;

    private final ModelMapper modelMapper;

    @Autowired
    public LocationController(LocationService locationService, CityService cityService, ModelMapper modelMapper) {
        this.locationService = locationService;
        this.cityService = cityService;
        this.modelMapper = modelMapper;
    }

    @GetMapping(value = "/locations/{id}")
    public ResponseEntity<LocationDto> get(@PathVariable Long id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Location location = locationService.get(id);
        if (location == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        LocationDto locationDto = modelMapper.map(location, LocationDto.class);
        return new ResponseEntity<>(locationDto, HttpStatus.OK);
    }

    @GetMapping(value = "/locations")
    public ResponseEntity<List<LocationDto>> getAll(@RequestParam(value = "cityId", required = false) Long cityId) {
        List<Location> locations;
        List<LocationDto> locationDtos;
        if (cityId == null) {
            locations = locationService.getAll();
        } else {
            locations = locationService.getByCityId(cityId);
        }
        if (locations.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        locationDtos = locations.stream()
                .map(location -> modelMapper.map(location, LocationDto.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(locationDtos, HttpStatus.OK);
    }

    @PostMapping(value = "/locations")
    public ResponseEntity<LocationDto> add(@RequestBody LocationDto locationDto) {
        Location newLocation = modelMapper.map(locationDto, Location.class);
        newLocation.setAddress(locationDto.getAddress());
        newLocation.setCity(cityService.get(locationDto.getCityId()));

        Location location = locationService.add(newLocation);
        locationDto = modelMapper.map(location, LocationDto.class);
        locationDto.setAddress(location.getAddress());
        locationDto.setCityId(location.getCity().getId());
        return new ResponseEntity<>(locationDto, HttpStatus.CREATED);
    }

    @PutMapping(value = "/locations/{id}")
    public ResponseEntity<IdDto> update(@PathVariable Long id, @RequestBody LocationDto locationDto) {
        Location location = locationService.update(modelMapper.map(locationDto, Location.class), id);
        return new ResponseEntity<>(new IdDto(location.getId()), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/locations/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Location location = locationService.get(id);
        if (location == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        this.locationService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}