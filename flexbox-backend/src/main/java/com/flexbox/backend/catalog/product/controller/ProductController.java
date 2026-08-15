package com.flexbox.backend.catalog.product.controller;

import com.flexbox.backend.catalog.product.dto.response.ProductResponse;
import com.flexbox.backend.admin.box.service.AdminProductService;
import com.flexbox.backend.common.dto.response.CollectionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/catalog/products")
public class ProductController {

    private final AdminProductService adminProductService;

    ProductController(AdminProductService adminProductService) {
        this.adminProductService = adminProductService;
    }

    @GetMapping
    public ResponseEntity <CollectionResponse<ProductResponse>> getProducts() {
        var products = adminProductService.getProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProductById(
            @PathVariable Long productId) {
        var product = adminProductService.getProductById(productId);
        return ResponseEntity.ok(product);
    }
}
