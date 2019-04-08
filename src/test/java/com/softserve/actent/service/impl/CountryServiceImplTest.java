package com.softserve.actent.service.impl;

import com.softserve.actent.exceptions.DataNotFoundException;
import com.softserve.actent.repository.CountryRepository;
import com.softserve.actent.service.CountryService;
import com.softserve.actent.model.entity.Country;
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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CountryServiceImplTest {
    @Autowired
    private CountryService countryService;

    @MockBean
    private CountryRepository countryRepository;

    @Mock
    private List<Country> countries;
    @Mock
    private Country firstCountry;
    @Mock
    private Country secondCountry;

    private final Long firstId = 1L;
    private final Long secondId = 2L;
    private final Long notExistedId = 100L;
    private final String firstCountryName = "Ukraine";
    private final String secondCountryName = "Canada";


    @Before
    public void setUp() {
        firstCountry = createCountry(firstId, firstCountryName);
        secondCountry = createCountry(secondId, secondCountryName);

        countries = Arrays.asList(firstCountry, secondCountry);

        when(countryRepository.findById(firstId)).thenReturn(Optional.of(firstCountry));
        when(countryRepository.findById(secondId)).thenReturn(Optional.of(secondCountry));

        when(countryRepository.findById(notExistedId)).thenReturn(Optional.empty());
        when(countryRepository.findAll()).thenReturn(countries);

        when(countryRepository.save(firstCountry)).thenReturn(firstCountry);
        when(countryRepository.save(secondCountry)).thenReturn(secondCountry);

        doNothing().when(countryRepository).deleteById(firstId);
        doNothing().when(countryRepository).deleteById(secondId);
    }

    @Test
    public void testGetById() {
        assertThat(countryService.get(firstId).getName()).isEqualTo(firstCountryName);
    }

    @Test(expected = DataNotFoundException.class)
    public void testGetNullId() {
        countryService.get(null);
    }

    @Test(expected = DataNotFoundException.class)
    public void testGetByNotExistedId_orThrowException() {
        countryService.get(notExistedId);
    }

    @Test
    public void testGetAll() {
        assertThat(countryService.getAll().size()).isEqualTo(countries.size());
        assertThat(countryService.getAll().get(0).getName()).isEqualTo(firstCountryName);
        assertThat(countryService.getAll().get(1).getName()).isEqualTo(secondCountryName);
    }

    @Test
    public void testAdd() {
        assertThat(countryService.add(firstCountry)).isEqualTo(firstCountry);
    }

    @Test
    public void testUpdate() {
        when(countryRepository.existsById(secondId)).thenReturn(true);
        assertThat(countryService.update(secondCountry, secondId).getName()).isEqualTo(secondCountryName);
    }

    @Test(expected = DataNotFoundException.class)
    public void testUpdateByNotExistedId_thenThrowException() {
        countryService.update(firstCountry, notExistedId);
    }

    @Test(expected = DataNotFoundException.class)
    public void testUpdateWhereCountryNull_thenThrowException() {
        countryService.update(null, firstId);
    }

    @Test
    public void testDeleteById() {
        countryService.delete(firstId);
    }

    @Test(expected = DataNotFoundException.class)
    public void testDeleteByNotExistedId_thenThrowException() {
        countryService.delete(notExistedId);
    }

    @Test(expected = DataNotFoundException.class)
    public void testDeleteByNullId() {
        countryService.delete(null);
    }

    private Country createCountry(Long countryId, String name) {
        Country country = new Country();
        country.setId(countryId);
        country.setName(name);
        return country;
    }

    @After
    public void tearDown() {
        firstCountry = null;
        secondCountry = null;
        assertNull(firstCountry);
        assertNull(secondCountry);
    }
}
