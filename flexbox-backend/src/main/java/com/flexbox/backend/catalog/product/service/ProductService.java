package com.flexbox.backend.catalog.product.service;

import com.flexbox.backend.catalog.product.dto.response.CategorySummaryResponse;
import com.flexbox.backend.catalog.product.dto.response.ProductResponse;
import com.flexbox.backend.catalog.product.repository.ProductRepository;
import com.flexbox.backend.common.dto.response.CollectionResponse;
import com.flexbox.backend.common.exception.ResourceNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public CollectionResponse<ProductResponse> getProducts() {
        var products = productRepository.findAll()
                .stream()
                .map(product -> ProductResponse.from(product,
                        CategorySummaryResponse.from(product.getCategory())))
                .toList();

        return new CollectionResponse<>(products);

    }

    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {
        var product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found with ID '%d'".formatted(id)));
        return ProductResponse.from(product, CategorySummaryResponse.from(product.getCategory()));

    }


}
