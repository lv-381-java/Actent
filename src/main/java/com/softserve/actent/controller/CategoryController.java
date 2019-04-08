package com.softserve.actent.controller;

import com.softserve.actent.constant.NumberConstants;
import com.softserve.actent.constant.StringConstants;
import com.softserve.actent.constant.UrlConstants;
import com.softserve.actent.model.dto.category.CategoryDto;
import com.softserve.actent.model.dto.IdDto;
import com.softserve.actent.model.dto.category.ShowCategoryDto;
import com.softserve.actent.model.dto.category.CreateCategoryDto;
import com.softserve.actent.model.entity.Category;
import com.softserve.actent.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Validated
@RequestMapping(UrlConstants.API_V1)
@PreAuthorize("permitAll()")
public class CategoryController {

    private final CategoryService categoryService;

    private final ModelMapper modelMapper;

    @Autowired
    public CategoryController(CategoryService categoryService, ModelMapper modelMapper) {
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
    }

    @PostMapping(value = "/categories")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("isAuthenticated()")
    public IdDto addCategory(@RequestBody @Validated CreateCategoryDto createCategoryDto) {
        Category parent = categoryService.getParent(createCategoryDto.getParentId());
        Category category = categoryService.add(new Category(createCategoryDto.getName(), parent));
        return new IdDto(category.getId());
    }

    @GetMapping(value = "/categories")
    @ResponseStatus(HttpStatus.OK)
    public List<ShowCategoryDto> getCategories() {
        List<Category> categories = categoryService.getAll();
        List<ShowCategoryDto> categoryDtos = new ArrayList<>();

        for (Category category : categories) {
            ShowCategoryDto showCategoryDto = new ShowCategoryDto();
            showCategoryDto.setId(category.getId());
            showCategoryDto.setName(category.getName());
            Category parent = category.getParent();
            if (parent != null) {
                showCategoryDto.setParentId(parent.getId());
            }
            categoryDtos.add(showCategoryDto);
        }
        return categoryDtos;
    }

    @GetMapping(value = "/categories/parentsubcategories")
    @ResponseStatus(HttpStatus.OK)
    public List<ShowCategoryDto> getParCategories() {
        List<Category> categories = categoryService.getParentCategories(null);
        return categories.stream()
                .map(category -> modelMapper.map(category, ShowCategoryDto.class))
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/categories/subcategories/{parentId}")
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryDto> getSubCategoriesById(@PathVariable @NotNull @Positive(message = StringConstants.CATEGORY_ID_MUST_BE_POSITIVE) Long parentId) {
        Category parent = categoryService.getParent(parentId);
        List<Category> subcategories = categoryService.getSubcategories(parent);
        return subcategories.stream()
                .map(category -> modelMapper.map(category, CategoryDto.class))
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/categories/subcategories")
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryDto> getSubCategoriesByName(@RequestParam @Size(min = NumberConstants.MIN_VALUE_FOR_CATEGORY_NAME, max = NumberConstants.MAX_VALUE_FOR_CATEGORY_NAME,
            message = StringConstants.CATEGORY_NO_LONGER_THAN_THIRTY_SYMBOLS) String parentCategoryName) {
        Category parent = categoryService.getParentByName(parentCategoryName);
        List<Category> subcategories = categoryService.getSubcategories(parent);
        return subcategories.stream()
                .map(category -> modelMapper.map(category, CategoryDto.class))
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/categories/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto getCategoryById(@PathVariable @NotNull @Positive(message = StringConstants.CATEGORY_ID_MUST_BE_POSITIVE) Long id) {
        Category category = categoryService.get(id);
        return modelMapper.map(category, CategoryDto.class);
    }

    @DeleteMapping(value = "/categories/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("isAuthenticated()")
    public void deleteCategoryById(@PathVariable @NotNull @Positive(message = StringConstants.CATEGORY_ID_MUST_BE_POSITIVE) Long id) {
        categoryService.delete(id);
    }

    @PutMapping(value = "/categories/{id}")
    @PreAuthorize("isAuthenticated()")
    public CreateCategoryDto update(@PathVariable @NotNull @Positive(message = StringConstants.CATEGORY_ID_MUST_BE_POSITIVE) Long id,
                                    @RequestBody @Validated CreateCategoryDto createCategoryDto) {
        Category parent = categoryService.getParent(createCategoryDto.getParentId());
        Category newCategory = new Category(createCategoryDto.getName(), parent);
        categoryService.update(newCategory, id);
        return modelMapper.map(createCategoryDto, CreateCategoryDto.class);
    }
}

