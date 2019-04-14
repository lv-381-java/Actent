package com.softserve.actent.controller;

import com.softserve.actent.constant.NumberConstants;
import com.softserve.actent.constant.StringConstants;
import com.softserve.actent.constant.UrlConstants;
import com.softserve.actent.model.dto.IdDto;
import com.softserve.actent.model.dto.location.LocationAddressDto;
import com.softserve.actent.model.dto.location.LocationCreateDto;
import com.softserve.actent.model.dto.location.LocationDto;
import com.softserve.actent.model.entity.Location;
import com.softserve.actent.service.LocationService;
import org.hibernate.validator.constraints.Length;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(UrlConstants.API_V1_LOCATIONS)
public class LocationController {
    private final LocationService locationService;
    private final ModelMapper modelMapper;

    @Autowired
    public LocationController(LocationService locationService, ModelMapper modelMapper) {
        this.locationService = locationService;
        this.modelMapper = modelMapper;
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public LocationDto getById(@PathVariable @NotNull(message = StringConstants.LOCATION_MUST_BE_NOT_NULL)
                               @Positive(message = StringConstants.LOCATION_ID_POSITIVE_AND_GREATER_THAN_ZERO) Long id) {
        Location location = locationService.get(id);
        return modelMapper.map(location, LocationDto.class);
    }

    @GetMapping(value = "autocomplete/{address}")
    @ResponseStatus(HttpStatus.OK)
    public List<LocationAddressDto> getAutocompleteLocations(@PathVariable
                                                             @NotBlank(message = StringConstants.EMPTY_LOCATION)
                                                             @Length(min = NumberConstants.LOCATION_MIN_LENGTH,
                                                                     max = NumberConstants.LOCATION_MAX_LENGTH,
                                                                     message = StringConstants.LOCATION_SHOULD_BE_BETWEEN_3_AND_100_SYMBOLS)
                                                                     String address) {
        List<Location> locations = locationService.getAllAutocomplete(address);
        return locations.stream()
                .map(location -> modelMapper.map(location, LocationAddressDto.class))
                .collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public IdDto add(@Validated @RequestBody LocationCreateDto locationCreateDto) {

        Location location = locationService.add(modelMapper.map(locationCreateDto, Location.class));
        return new IdDto(location.getId());
    }

}
