package com.softserve.actent.controller;

import com.softserve.actent.constant.StringConstants;
import com.softserve.actent.constant.UrlConstants;
import com.softserve.actent.model.dto.IdDto;
import com.softserve.actent.model.dto.location.RegionCreateDto;
import com.softserve.actent.model.dto.location.RegionDto;
import com.softserve.actent.model.dto.location.RegionUpdateDto;
import com.softserve.actent.model.entity.Region;
import com.softserve.actent.service.RegionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(UrlConstants.API_V1_REGIONS)
public class RegionController {

    private final RegionService regionService;
    private final ModelMapper modelMapper;

    @Autowired
    public RegionController(RegionService regionService, ModelMapper modelMapper) {
        this.regionService = regionService;
        this.modelMapper = modelMapper;
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RegionDto getById(@PathVariable @NotNull(message = StringConstants.REGION_ID_NOT_NULL)
                             @Positive(message = StringConstants.REGION_ID_POSITIVE_AND_GREATER_THAN_ZERO) Long id) {

        Region region = regionService.get(id);
        return modelMapper.map(region, RegionDto.class);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<RegionDto> getAll(@RequestParam(value = "countryId", required = false)
                                  @Positive(message = StringConstants.COUNTRY_ID_POSITIVE_AND_GREATER_THAN_ZERO) Long countryId) {
        List<Region> regions;
        List<RegionDto> regionDtos;
        if (countryId == null) {
            regions = regionService.getAll();
        } else {
            regions = regionService.getAllByCountryId(countryId);
        }
        regionDtos = regions.stream()
                .map(region -> modelMapper.map(region, RegionDto.class))
                .collect(Collectors.toList());
        return regionDtos;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public IdDto add(@Validated @RequestBody RegionCreateDto regionCreateDto) {

        Region region = regionService.add(modelMapper.map(isRegionExist(regionCreateDto), Region.class));
        return new IdDto(region.getId());
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RegionUpdateDto updateName(@PathVariable Long id,
                                      @Validated @RequestBody RegionUpdateDto regionUpdateDto) {
        Region region = regionService.update(modelMapper.map(checkUpdatedRegion(regionUpdateDto), Region.class), id);
        return modelMapper.map(region, RegionUpdateDto.class);
    }


    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @NotNull(message = StringConstants.COUNTRY_ID_NOT_NULL)
                       @Positive(message = StringConstants.COUNTRY_ID_POSITIVE_AND_GREATER_THAN_ZERO) Long id) {

        regionService.delete(id);
    }

    private RegionCreateDto isRegionExist(RegionCreateDto regionCreateDto) {
        regionCreateDto.setName(regionCreateDto.getName().toLowerCase().trim());
        return regionCreateDto;
    }

    private RegionUpdateDto checkUpdatedRegion(RegionUpdateDto regionUpdateDto) {
        regionUpdateDto.setName(regionUpdateDto.getName().toLowerCase().trim());
        return regionUpdateDto;
    }
}

