package com.softserve.actent.controller;

import com.softserve.actent.model.dto.RegionDto;
import com.softserve.actent.model.entity.Region;
import com.softserve.actent.service.CountryService;
import com.softserve.actent.service.RegionService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class RegionController {

    private final RegionService regionService;

    private final CountryService countryService;

    private final ModelMapper modelMapper;

    public RegionController(RegionService regionService, CountryService countryService, ModelMapper modelMapper) {
        this.regionService = regionService;
        this.countryService = countryService;
        this.modelMapper = modelMapper;
    }


    @GetMapping(value = "/regions/{id}")
    public ResponseEntity<RegionDto> get(@PathVariable Long id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Region region = regionService.get(id);
        if (region == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        RegionDto regionDto = modelMapper.map(region, RegionDto.class);
        return new ResponseEntity<>(regionDto, HttpStatus.OK);
    }


    @GetMapping(value = "/regions")
    public ResponseEntity<List<RegionDto>> getAll(@RequestParam(value = "countryId", required = false) Long countryId) {
        List<Region> regions;
        List<RegionDto> regionDtos;
        if (countryId == null) {
            regions = regionService.getAll();
        } else {
            regions = regionService.getByCountryId(countryId);
            if (regions.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        regionDtos = regions.stream()
                .map(region -> modelMapper.map(region, RegionDto.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(regionDtos, HttpStatus.CREATED);
    }

    @PostMapping(value = "/regions")
    public ResponseEntity<RegionDto> add(@RequestBody RegionDto regionDto) {
        Region newRegion = modelMapper.map(regionDto, Region.class);
        newRegion.setName(regionDto.getName());
        newRegion.setCountry(countryService.get(regionDto.getCountryId()));

        Region region = regionService.add(newRegion);
        regionDto = modelMapper.map(region, RegionDto.class);
        regionDto.setName(region.getName());
        regionDto.setCountryId(region.getCountry().getId());
        return new ResponseEntity<>(regionDto, HttpStatus.CREATED);
    }

    @PutMapping(value = "/regions/{id}")
    public ResponseEntity<RegionDto> update(@PathVariable Long id, @RequestBody RegionDto regionDto) {
        Region region = regionService.update(modelMapper.map(regionDto, Region.class), id);
        return new ResponseEntity<>(modelMapper.map(region, RegionDto.class), HttpStatus.OK);
    }

    @DeleteMapping(value = "/regions/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Region region = regionService.get(id);
        if (region == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        regionService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}