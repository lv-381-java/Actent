package com.softserve.actent.service.impl;

import com.softserve.actent.exceptions.DataNotFoundException;
import com.softserve.actent.model.entity.Country;
import com.softserve.actent.model.entity.Region;
import com.softserve.actent.repository.CountryRepository;
import com.softserve.actent.repository.RegionRepository;
import com.softserve.actent.service.RegionService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static junit.framework.TestCase.assertNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RegionServiceImplTest {
    @Autowired
    private RegionService regionService;

    @MockBean
    private RegionRepository regionRepository;
    @MockBean
    private CountryRepository countryRepository;

    @Mock
    private List<Region> regions;
    @Mock
    private Country country;
    @Mock
    private Region region;
    private final Long id = 1L;
    private final Long notExistedId = 2L;
    private final String name = "Tohoku";
    private final int index = 0;

    @Before
    public void setUp() {
        when(regions.get(index)).thenReturn(region);
        when(regionRepository.findAll()).thenReturn(regions);

        when(country.getId()).thenReturn(id);
        when(region.getId()).thenReturn(id);
        when(region.getName()).thenReturn(name);
        when(region.getCountry()).thenReturn(country);

        when(countryRepository.existsById(id)).thenReturn(true);
        when(regionRepository.existsById(id)).thenReturn(true);
        when(regionRepository.existsById(notExistedId)).thenReturn(false);

        when(regionRepository.findById(id)).thenReturn(Optional.of(region));
        when(regionRepository.findById(notExistedId)).thenReturn(Optional.empty());

        when(regionRepository.save(region)).thenReturn(region);
        doNothing().when(regionRepository).deleteById(id);
    }

    @Test
    public void testGetById() {
        assertThat(regionService.get(id).getName()).isEqualTo(name);
    }

    @Test(expected = DataNotFoundException.class)
    public void testGetNullId() {
        regionService.get(null);
    }

    @Test(expected = DataNotFoundException.class)
    public void testGetByNotExistedId_orThrowException() {
        regionService.get(notExistedId);
    }

    @Test
    public void testGetAll() {
        assertThat(regionService.getAll().get(index)).isEqualTo(region);
    }

    @Test(expected = DataNotFoundException.class)
    public void testUpdateByNotExistedId_thenThrowException() {
        regionService.update(region, notExistedId);
    }

    @Test(expected = NullPointerException.class)
    public void testUpdateWhereRegionNull_thenThrowException() {
        regionService.update(null, id);
    }

    @Test
    public void testDeleteById() {
        regionService.delete(id);
    }

    @Test(expected = DataNotFoundException.class)
    public void testDeleteByNotExistedId_thenThrowException() {
        regionService.delete(notExistedId);
    }

    @Test(expected = DataNotFoundException.class)
    public void testDeleteByNullId() {
        regionService.delete(null);
    }

    @After
    public void tearDown() {
        region = null;
        assertNull(region);
    }
}
