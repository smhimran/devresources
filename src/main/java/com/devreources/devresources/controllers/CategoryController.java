package com.devreources.devresources.controllers;

import com.devreources.devresources.models.Category;
import com.devreources.devresources.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {

    @Autowired
    CategoryRepository repository;

    @GetMapping("/")
    public List<Category> getAllCategories() {
        return repository.findAll();
    }
}
