package com.softserve.actent.utils.converters;

import com.softserve.actent.model.dto.CountryDto;
import com.softserve.actent.model.entity.Country;
import com.softserve.actent.utils.modelmapper.IModelMapperConverter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CountryConverter implements IModelMapperConverter<Country, CountryDto> {
    private final ModelMapper modelMapper;

    @Autowired
    public CountryConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public Country convertToEntity(CountryDto countryDto) {
        Country country = modelMapper.map(countryDto, Country.class);
        return country;
    }

    @Override
    public CountryDto convertToDto(Country country) {
        CountryDto countryDto = modelMapper.map(country, CountryDto.class);
        return countryDto;
    }
}
