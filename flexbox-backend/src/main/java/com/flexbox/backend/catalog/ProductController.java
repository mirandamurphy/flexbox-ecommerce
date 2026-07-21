package com.flexbox.backend.catalog;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
class ProductController {
    private final ProductRepository productRepository;


    ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
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
