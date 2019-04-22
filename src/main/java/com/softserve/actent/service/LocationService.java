package com.softserve.actent.service;

import com.softserve.actent.model.entity.Location;

import java.util.List;

public interface LocationService {

    Location get(Long id);

    Location getByAddress(String address);

    Location getByCoordinates(String address);

    List<Location> getAllAutocomplete(String address);
}

