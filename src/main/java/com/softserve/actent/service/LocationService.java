package com.softserve.actent.service;

import com.softserve.actent.model.entity.Location;

import java.util.List;

public interface LocationService {
    Location add(String address);

    Location get(Long id);

    Location getByAddress(String address);

    List<Location> getAllAutocomplete(String address);
}

