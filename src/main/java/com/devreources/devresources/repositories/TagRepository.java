package com.devreources.devresources.repositories;

import com.devreources.devresources.models.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Integer> {
    List<Tag> findAllByTitleContainsIgnoreCase(String title);
}
