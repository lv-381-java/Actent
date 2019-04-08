package com.softserve.actent.service.impl;

import com.softserve.actent.constant.ExceptionMessages;
import com.softserve.actent.exceptions.DataNotFoundException;
import com.softserve.actent.exceptions.codes.ExceptionCode;
import com.softserve.actent.model.entity.Region;
import com.softserve.actent.repository.CountryRepository;
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
    private final CountryRepository countryRepository;

    @Autowired
    public RegionServiceImpl(RegionRepository regionRepository, CountryRepository countryRepository) {
        this.regionRepository = regionRepository;
        this.countryRepository = countryRepository;
    }

    @Transactional
    @Override
    public Region add(Region region) {
        Region newRegion = new Region();
        newRegion.setName(region.getName());

        if (countryRepository.existsById(region.getCountry().getId())) {
            newRegion.setCountry(region.getCountry());
            return regionRepository.save(newRegion);
        } else {
            throw new DataNotFoundException(
                    ExceptionMessages.COUNTRY_NOT_FOUND,
                    ExceptionCode.NOT_FOUND);
        }
    }

    @Transactional
    @Override
    public Region update(Region region, Long regionId) {

        if (regionRepository.existsById(regionId)) {
            Region dbRegion = regionRepository.getOne(regionId);
            dbRegion.setName(region.getName());
            return regionRepository.save(dbRegion);
        } else {
            throw new DataNotFoundException(
                    ExceptionMessages.REGION_NOT_FOUND,
                    ExceptionCode.NOT_FOUND);
        }
    }

    @Override
    public Region get(Long regionId) {
        return regionRepository.findById(regionId)
                .orElseThrow(() -> new DataNotFoundException(
                        ExceptionMessages.REGION_NOT_FOUND,
                        ExceptionCode.NOT_FOUND));
    }

    @Override
    public List<Region> getAll() {
       return regionRepository.findAll();
    }

    @Transactional
    @Override
    public void delete(Long regionId) {
        Optional<Region> region = regionRepository.findById(regionId);
        if (region.isPresent()) {
            regionRepository.deleteById(regionId);
        } else {
            throw new DataNotFoundException(
                    ExceptionMessages.REGION_NOT_FOUND,
                    ExceptionCode.NOT_FOUND);
        }
    }

    @Override
    public List<Region> getAllByCountryId(Long countryId) {
        return regionRepository.findAllByCountry_Id(countryId);
    }
}
