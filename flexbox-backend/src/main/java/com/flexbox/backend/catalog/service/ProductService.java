package com.flexbox.backend.catalog.service;

import com.flexbox.backend.catalog.dto.category.CategorySummary;
import com.flexbox.backend.catalog.dto.product.ProductDetail;
import com.flexbox.backend.catalog.dto.product.ProductSummary;
import com.flexbox.backend.catalog.exception.ProductNotFoundException;
import com.flexbox.backend.catalog.repository.ProductRepository;
import com.flexbox.backend.catalog.response.ProductListResponse;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public ProductListResponse getAllProducts() {
        var products = productRepository.findAll()
                .stream()
                .map(product -> ProductSummary.from(product, CategorySummary.from(product.getCategory())))
                .toList();
        return new ProductListResponse(products);

    }

    @Transactional(readOnly = true)
    public ProductDetail getProductById(Long id) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + id));
        return ProductDetail.from(product, CategorySummary.from(product.getCategory()));

    }


}
