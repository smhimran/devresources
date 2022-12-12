package com.devreources.devresources.controllers.category;

import com.devreources.devresources.models.Category;
import com.devreources.devresources.repositories.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {
    Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    CategoryRepository repository;

    @GetMapping("/")
    public List<Category> getAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable int id) {
        Optional<Category> category = repository.findById(id);
        if (category.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(category);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No such category!");
    }

    @PostMapping("/")
    @PreAuthorize("hasAuthority('WRITE')")
    public ResponseEntity<?> create(@RequestBody CatergoryRequest request) {
        try {
            Category category = new Category(request.getTitle());
            repository.save(category);
            return ResponseEntity.status(HttpStatus.CREATED).body(category);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Category already exists!");
        } catch (Exception e) {
            logger.error("Error creating category {}", request.getTitle(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating category");
        }
    }
}
