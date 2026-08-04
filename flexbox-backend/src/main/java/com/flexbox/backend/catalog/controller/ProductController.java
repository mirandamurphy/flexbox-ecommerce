package com.flexbox.backend.catalog.controller;

import com.flexbox.backend.catalog.dto.product.ProductDetail;
import com.flexbox.backend.catalog.response.ProductListResponse;
import com.flexbox.backend.catalog.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    ProductController(ProductService productService) {
        this.productService = productService;
    }


    @GetMapping
    public ResponseEntity<ProductListResponse> getAllProducts() {
        var products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDetail> getProductById(
            @PathVariable("id") Long id) {
        var product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }


}
