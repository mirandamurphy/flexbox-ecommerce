package com.flexbox.backend.catalog.service;

import com.flexbox.backend.catalog.dto.product.ProductDetail;
import com.flexbox.backend.catalog.exception.ProductNotFoundException;
import com.flexbox.backend.catalog.repository.ProductRepository;
import com.flexbox.backend.catalog.response.ProductListResponse;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductListResponse getAllProducts() {

    }
    public ProductDetail getProductById(Long id) {
        return productRepository.findById(id)
                .map(ProductDetail::from)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
    }


}
