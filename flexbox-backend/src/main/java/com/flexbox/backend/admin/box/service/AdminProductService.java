package com.flexbox.backend.admin.box.service;

import com.flexbox.backend.admin.box.dto.response.CategoryResponse;
import com.flexbox.backend.admin.box.dto.response.ProductDetailResponse;
import com.flexbox.backend.admin.box.dto.response.ProductInventoryResponse;
import com.flexbox.backend.admin.box.dto.response.ProductSummaryResponse;
import com.flexbox.backend.catalog.product.repository.ProductInventoryRepository;
import com.flexbox.backend.catalog.product.repository.CategoryRepository;
import com.flexbox.backend.catalog.product.repository.ProductRepository;
import com.flexbox.backend.common.dto.response.CollectionResponse;
import com.flexbox.backend.common.exception.ResourceNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

@Service
public class AdminProductService {

    private final ProductRepository productRepository;
    private final ProductInventoryRepository inventoryRepository;

    public AdminProductService(ProductRepository productRepository, ProductInventoryRepository inventoryRepository) {
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
    }

    @Transactional(readOnly = true)
    public CollectionResponse<ProductSummaryResponse> getProducts() {
        var products = productRepository.findAll()
                .stream()
                .map(ProductSummaryResponse::from)
                .toList();

        return new CollectionResponse<>(products);

    }

    @Transactional(readOnly = true)
    public ProductDetailResponse getProductById(Long productId) {
        var product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found with ID '%d'".formatted(productId)));

        var inventory = inventoryRepository.findByProduct_Id(productId)
                .map(
                        inv -> new ProductInventoryResponse(inv.getInStock(), inv.getReserved()))
                .orElse(new ProductInventoryResponse(0, 0));

        var category = product.getCategory();

        return ProductDetailResponse.from(product,
                new CategoryResponse(category.getId(), category.getName()), inventory);
    }

    @Transactional
    public void deactivateProductById(Long productId) {
        if(!productRepository.updateIsActiveById(false,productId)) {
            throw new ResourceNotFoundException("Product not found with ID '%d'".formatted(productId));
        }
    }






}
