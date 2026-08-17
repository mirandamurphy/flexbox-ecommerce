package com.flexbox.backend.admin.catalog.service;

import com.flexbox.backend.admin.catalog.dto.response.AdminCategoryResponse;
import com.flexbox.backend.admin.catalog.dto.response.AdminProductDetailResponse;
import com.flexbox.backend.admin.catalog.dto.response.AdminProductInventoryResponse;
import com.flexbox.backend.admin.catalog.dto.response.AdminProductSummaryResponse;
import com.flexbox.backend.catalog.product.repository.ProductInventoryRepository;
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
    public CollectionResponse<AdminProductSummaryResponse> getProducts() {
        var products = productRepository.findAll()
                .stream()
                .map(AdminProductSummaryResponse::from)
                .toList();

        return new CollectionResponse<>(products);

    }

    @Transactional(readOnly = true)
    public AdminProductDetailResponse getProductById(Long productId) {
        var product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found with ID '%d'".formatted(productId)));

        var inventory = inventoryRepository.findByProduct_Id(productId)
                .map(
                        inv -> new AdminProductInventoryResponse(inv.getInStock(), inv.getReserved()))
                .orElse(new AdminProductInventoryResponse(0, 0));

        var category = product.getCategory();

        return AdminProductDetailResponse.from(product,
                new AdminCategoryResponse(category.getId(), category.getName()), inventory);
    }

    @Transactional
    public void deactivateProductById(Long productId) {
        if(productRepository.updateIsActiveById(false,productId) == 0) {
            throw new ResourceNotFoundException("Product not found with ID '%d'".formatted(productId));
        }
    }






}
