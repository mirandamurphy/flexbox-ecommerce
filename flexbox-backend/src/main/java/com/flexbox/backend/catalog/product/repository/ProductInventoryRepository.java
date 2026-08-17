package com.flexbox.backend.catalog.product.repository;


import com.flexbox.backend.catalog.product.model.ProductInventory;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface ProductInventoryRepository extends JpaRepository<ProductInventory, Long> {
    Optional<ProductInventory> findByProduct_Id(Long id);


}