package com.softserve.actent.service;

import com.softserve.actent.model.entity.Region;

import java.util.List;

public interface RegionService extends BaseCrudService<Region> {
    List<Region> getByCountryId(Long countryId);
}