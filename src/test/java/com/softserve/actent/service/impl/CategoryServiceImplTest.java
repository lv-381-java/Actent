package com.softserve.actent.service.impl;

import com.softserve.actent.exceptions.DataNotFoundException;
import com.softserve.actent.model.entity.Category;
import com.softserve.actent.repository.CategoryRepository;
import com.softserve.actent.service.CategoryService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static junit.framework.TestCase.assertNull;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryServiceImplTest {

    @Autowired
    private CategoryService categoryService;

    @MockBean
    private CategoryRepository categoryRepository;

    private final Long firstCategoryId = 1L;
    private final Long secondCategoryId = 2L;
    private final Long nonExistingCategoryId = 3L;
    private final String firstCategoryName = "sport";
    private final String secondCategoryName = "football";
    private final String nonExistingCategoryName = "noncategory";
    private final Category firstCategoryParent = null;
    private final Category nonExistingCategoryParent = null;
    private Category firstCategory;
    private Category secondCategory;
    private Category nonExistingCategory;
    private List<Category> categories;

    @Before
    public void setUp() {

        firstCategory = new Category();
        firstCategory.setId(firstCategoryId);
        firstCategory.setName(firstCategoryName);
        firstCategory.setParent(firstCategoryParent);

        secondCategory = new Category();
        secondCategory.setId(secondCategoryId);
        secondCategory.setName(secondCategoryName);
        secondCategory.setParent(firstCategory);

        categories = Arrays.asList(firstCategory, secondCategory);

        Mockito.when(categoryRepository.findById(firstCategoryId)).thenReturn(Optional.of(firstCategory));
        Mockito.when(categoryRepository.findById(secondCategoryId)).thenReturn(Optional.of(secondCategory));
        Mockito.when(categoryRepository.findById(nonExistingCategoryId)).thenReturn(Optional.empty());

        Mockito.when(categoryRepository.findByName(firstCategoryName)).thenReturn(firstCategory);
        Mockito.when(categoryRepository.findByName(secondCategoryName)).thenReturn(secondCategory);
        Mockito.when(categoryRepository.findByName(nonExistingCategoryName)).thenReturn(nonExistingCategory);

        Mockito.when(categoryRepository.findAllByParent(firstCategory)).thenReturn(categories);
        Mockito.when(categoryRepository.findAllByParent(secondCategory)).thenReturn(categories);
        Mockito.when(categoryRepository.findAllByParent(nonExistingCategory)).thenReturn(categories);

        Mockito.when(categoryRepository.findAll()).thenReturn(categories);

        Mockito.when(categoryRepository.save(firstCategory)).thenReturn(firstCategory);
        Mockito.when(categoryRepository.save(secondCategory)).thenReturn(secondCategory);

        Mockito.doNothing().when(categoryRepository).deleteById(firstCategoryId);
        Mockito.doNothing().when(categoryRepository).deleteById(secondCategoryId);
    }

    @Test
    public void whenExistingId_thenCategoryShouldBeFound() {
        assertThat(categoryService.get(firstCategoryId).getName()).isEqualTo(firstCategoryName);
    }

    @Test(expected = DataNotFoundException.class)
    public void whenNonExistingId_thenExceptionShouldBeThrown() {
        categoryService.get(nonExistingCategoryId);
    }

    @Test
    public void whenExistingName_thenCategoryShouldBeFound() {
        assertThat(categoryService.getParentByName(firstCategoryName)).isEqualTo(firstCategory);
    }

    @Test(expected = DataNotFoundException.class)
    public void whenNonExistingCategoryName_thenExceptionShouldBeThrown() {
        categoryService.getParentByName(nonExistingCategoryName);
    }

    @Test
    public void whenGetAll_thenListOfCategoriesShouldBeReturned() {
        assertThat(categoryService.getAll().size()).isEqualTo(categories.size());
        assertThat(categoryService.getAll().get(0).getName()).isEqualTo(firstCategoryName);
        assertThat(categoryService.getAll().get(1).getName()).isEqualTo(secondCategoryName);
    }

    @Test
    public void whenAddCategory_thenCategoryShouldBeReturned() {
        assertThat(categoryService.add(firstCategory)).isEqualTo(firstCategory);
    }

    @Test
    public void whenUpdateCategoryWithExistingId_thenCategoryShouldBeReturned() {
        assertThat(categoryService.update(secondCategory, secondCategoryId).getName()).isEqualTo(secondCategoryName);
    }

    @Test(expected = DataNotFoundException.class)
    public void whenUpdateCAtegoryWithNonExistingId_thenExceptionShouldBeThrown() {
        categoryService.update(firstCategory, nonExistingCategoryId);
    }

    @Test
    public void whenDeleteCategoryWithExistingId_thenNothingShouldBeReturned() {
        categoryService.delete(firstCategoryId);
    }

    @Test(expected = DataNotFoundException.class)
    public void whenDeleteCategoryWithNonExistingId_thenExceptionShouldBeThrown() {
        categoryService.delete(nonExistingCategoryId);
    }

    @Test(expected = DataNotFoundException.class)
    public void whenGetSubcategoriesFromNonExistingParentCategory_thenExceptionShouldBeThrown() {
        categoryService.getSubcategories(nonExistingCategory);
    }

    @After
    public void tearDown() {
        firstCategory = null;
        secondCategory = null;
        assertNull(firstCategory);
        assertNull(secondCategory);
    }
}
