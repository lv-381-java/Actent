package com.softserve.actent.service;

import com.softserve.actent.model.entity.Location;

import java.util.List;

public interface LocationService extends BaseCrudService<Location> {
    List<Location> getByCityId(Long cityId);
}

