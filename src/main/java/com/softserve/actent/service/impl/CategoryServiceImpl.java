package com.softserve.actent.service.impl;

import com.softserve.actent.constant.ExceptionMessages;
import com.softserve.actent.exceptions.DataNotFoundException;
import com.softserve.actent.exceptions.codes.ExceptionCode;
import com.softserve.actent.model.entity.Category;
import com.softserve.actent.repository.CategoryRepository;
import com.softserve.actent.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public Category add(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public Category update(Category category, Long id) {
        if (categoryRepository.findById(id).isPresent()) {
            category.setId(id);
            return categoryRepository.save(category);
        } else {
            throw new DataNotFoundException(ExceptionMessages.CATEGORY_IS_NOT_FOUND, ExceptionCode.NOT_FOUND);
        }
    }

    @Override
    public Category get(Long id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        return optionalCategory.orElseThrow(() -> new DataNotFoundException(ExceptionMessages.CATEGORY_IS_NOT_FOUND, ExceptionCode.NOT_FOUND));
    }

    @Override
    public Category getParent(Long id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        return optionalCategory.orElse(null);
    }

    @Override
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isPresent()) {
            categoryRepository.deleteById(id);
        } else {
            throw new DataNotFoundException(ExceptionMessages.CATEGORY_IS_NOT_FOUND, ExceptionCode.NOT_FOUND);
        }
    }

    @Override
    public List<Category> getSubcategories(Category parent) {
        if (parent != null) {
            return categoryRepository.findAllByParent(parent);
        } else {
            throw new DataNotFoundException(ExceptionMessages.NO_SUBCATEGORIES_FOUND, ExceptionCode.NOT_FOUND);
        }
    }

    @Override
    public List<Category> getParentCategories(Category parent) {
        if (parent == null) {
            return categoryRepository.findAllByParent(parent);
        } else {
            throw new DataNotFoundException(ExceptionMessages.NO_SUBCATEGORIES_FOUND, ExceptionCode.NOT_FOUND);
        }
    }

    @Override
    public Category getParentByName(String name) {
        Category category = categoryRepository.findByName(name);
        if (category == null) {
            throw new DataNotFoundException(ExceptionMessages.NO_SUBCATEGORIES_FOUND, ExceptionCode.NOT_FOUND);
        }
        return category;
    }
}
