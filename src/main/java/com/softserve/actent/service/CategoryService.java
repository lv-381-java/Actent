package com.softserve.actent.service;

import com.softserve.actent.model.entity.Category;

import java.util.List;

public interface CategoryService extends BaseCrudService<Category> {
    List<Category> getSubcategories(Category parent);
    List<Category> getParentCategories(Category parent);
    Category getParentByName(String name);
    Category getParent(Long id);
}

