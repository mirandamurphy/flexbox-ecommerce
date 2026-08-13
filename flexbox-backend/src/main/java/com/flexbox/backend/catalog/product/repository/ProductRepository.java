package com.flexbox.backend.catalog.product.repository;

import com.flexbox.backend.catalog.product.model.Product;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {


    @NonNull
    @EntityGraph(attributePaths = "category")
    Optional<Product> findById(@NonNull Long id);


}