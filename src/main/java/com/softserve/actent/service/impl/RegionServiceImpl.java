package com.softserve.actent.service.impl;

import com.softserve.actent.model.entity.Region;
import com.softserve.actent.repository.RegionRepository;
import com.softserve.actent.service.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class RegionServiceImpl implements RegionService {

    private final RegionRepository regionRepository;

    @Autowired
    public RegionServiceImpl(RegionRepository regionRepository) {
        this.regionRepository = regionRepository;
    }

    @Override
    public Region get(Long regionId) {
        Optional<Region> optionalRegion = regionRepository.findById(regionId);
        return optionalRegion.orElse(null);
    }

    @Transactional
    @Override
    public Region update(Region region, Long id) {
        if (regionRepository.findById(id).isPresent()) {
            region.setId(id);
            return regionRepository.save(region);
        } else {
            // TODO: else throw exception
            return null;
        }
    }

    @Override
    public List<Region> getAll() {
        return regionRepository.findAll();
    }

    @Transactional
    @Override
    public Region add(Region region) {
        return regionRepository.save(region);
    }

    @Transactional
    @Override
    public void delete(Long regionId) {
        regionRepository.deleteById(regionId);
    }

    @Override
    public List<Region> getByCountryId(Long countryId) {
        return regionRepository.findAllByCountryId(countryId);
    }
}