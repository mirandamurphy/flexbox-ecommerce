package com.flexbox.backend.catalog;

import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // GET /products/{id}
    //Optional<Product> findById(Long id);

    // GET /products
    //Page<Product> findAll(Pageable pageable); // pagination + sort

    // GET /products?name=
    List<Product> findByNameIgnoreCase(String name);
    List<Product> findAllByNameContainingIgnoreCase(String name, Sort sort, Limit limit);

    // GET /categories/{categoryId}/items
    List<Product> findAllByCategoryId(Long categoryId, Sort sort, Limit limit);

    // GET /admin (cost filters for rev/profit)
    List<Product> findAllByCostPerUnitBetween(BigDecimal costPerUnitAfter, BigDecimal costPerUnitBefore, Sort sort, Limit limit);
    Product findAllByCostPerUnitLessThan(BigDecimal costPerUnitIsLessThan, Sort sort, Limit limit);
    Product findAllByCostPerUnitGreaterThan(BigDecimal costPerUnitIsGreaterThan, Sort sort, Limit limit);

    Optional<Product> findBySkuIgnoreCase(String sku);
    List<Product> findAllByIsActive(Boolean isActive, Sort sort, Limit limit);


    void deleteProductBySkuIgnoreCase(String sku);
}