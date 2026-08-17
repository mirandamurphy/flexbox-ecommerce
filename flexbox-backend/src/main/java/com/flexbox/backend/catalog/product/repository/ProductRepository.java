package com.flexbox.backend.catalog.product.repository;

import com.flexbox.backend.catalog.product.model.Product;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Transactional
    @Modifying
    @Query("update Product p set p.isActive = :isActive where p.id = :id")
    int updateIsActiveById(
            @Param("isActive")Boolean isActive, @Param("id") Long id);

    @NonNull
    @EntityGraph(attributePaths = "category")
    Optional<Product> findById(@NonNull Long id);


}