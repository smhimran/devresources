package com.devreources.devresources.controllers.tag;

import com.devreources.devresources.controllers.tag.request.TagRequest;
import com.devreources.devresources.models.Tag;
import com.devreources.devresources.repositories.TagRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/tag")
public class TagController {
    Logger logger = LoggerFactory.getLogger(TagController.class);

    @Autowired
    private TagRepository repository;

    @GetMapping("/")
    public List<Tag> getAll() {
        return repository.findAll();
    }

    @GetMapping("")
    public List<Tag> getByTitle(@RequestParam String title) {
        return repository.findAllByTitleContainsIgnoreCase(title);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable int id) {
        Optional<Tag> tagOptional = repository.findById(id);
        if (tagOptional.isPresent()) {
            Tag tag = tagOptional.get();
            return ResponseEntity.status(HttpStatus.OK).body(tag);
        } else {
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("No such tag!");
        }
    }

    @PostMapping("/")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_MODERATOR', 'ROLE_ADMIN')")
    public ResponseEntity<?> create(@RequestBody TagRequest request) {
        try {
            Tag tag = new Tag(request.getTitle());
            repository.save(tag);
            return ResponseEntity.status(HttpStatus.CREATED).body(tag);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Tag already exists!");
        } catch (Exception e) {
            logger.error("Error creating tag: {}", request.getTitle(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create tag!");
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_MODERATOR', 'ROLE_ADMIN')")
    public ResponseEntity<?> update(@RequestBody TagRequest request, @PathVariable int id) {
        Optional<Tag> tagOptional = repository.findById(id);
        if (tagOptional.isPresent()) {
            Tag tag = tagOptional.get();
            tag.setTitle(request.getTitle());
            try {
                repository.save(tag);
                return ResponseEntity.status(HttpStatus.OK).body(tag);
            } catch (Exception e) {
                logger.error("Error updating tag: {}", request.getTitle(), e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed updating tag!");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No such tag!");
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_MODERATOR', 'ROLE_ADMIN')")
    public ResponseEntity<?> delete(@PathVariable int id) {
        try {
            repository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body("Tag deleted successfully!");
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No such tag!");
        } catch (Exception e) {
            logger.error("Error deleting tag: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed deleting tag!");
        }
    }
}
