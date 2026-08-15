package com.flexbox.backend.catalog.product.controller;

import com.flexbox.backend.catalog.product.dto.response.ProductResponse;
import com.flexbox.backend.catalog.product.service.ProductService;
import com.flexbox.backend.common.dto.response.CollectionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/catalog/products")
public class ProductController {

    private final ProductService productService;

    ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity <CollectionResponse<ProductResponse>> getAllProducts() {
        var products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProductById(
            @PathVariable Long productId) {
        var product = productService.getProductById(productId);
        return ResponseEntity.ok(product);
    }
}
