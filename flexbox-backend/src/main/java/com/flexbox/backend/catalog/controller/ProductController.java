package com.flexbox.backend.catalog.controller;

import com.flexbox.backend.catalog.dto.product.ProductDetail;
import com.flexbox.backend.catalog.exception.ProductNotFoundException;
import com.flexbox.backend.catalog.repository.ProductRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
class ProductController {
    private final ProductRepository productRepository;


    ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDetail> getProductById(@PathVariable Long id) {
        var product = productRepository.findById(id) // TODO: moving to Service class
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
        return ResponseEntity.ok(product);
    }

//
//    @GetMapping
//    public Page<Product> getProducts(
//            @RequestParam(required = false) String name,
//            Pageable pageable) {
//
//        if(name != null && !name.isBlank()) {
//            return
//        }
//
//    }
}
