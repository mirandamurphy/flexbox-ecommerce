package com.flexbox.backend.catalog.product.repository;

import com.flexbox.backend.catalog.product.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}