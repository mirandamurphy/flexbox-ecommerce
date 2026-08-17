package com.flexbox.backend.admin.catalog.controller;

import com.flexbox.backend.admin.catalog.dto.response.AdminProductDetailResponse;
import com.flexbox.backend.admin.catalog.dto.response.AdminProductSummaryResponse;
import com.flexbox.backend.admin.catalog.service.AdminProductService;
import com.flexbox.backend.common.dto.response.CollectionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/products")
public class AdminProductController {

    private final AdminProductService adminProductService;

    public AdminProductController(AdminProductService adminProductService) {
        this.adminProductService = adminProductService;
    }

    @GetMapping
    public ResponseEntity<CollectionResponse<AdminProductSummaryResponse>> getProducts() {
        return ResponseEntity.ok(adminProductService.getProducts());
    }

    @GetMapping("/{productId}")
    public ResponseEntity<AdminProductDetailResponse> getProductById(
            @PathVariable Long productId
    ) {
        return ResponseEntity.ok(adminProductService.getProductById(productId));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deactivateProductById(
            @PathVariable Long productId
    ){
        adminProductService.deactivateProductById(productId);
        return ResponseEntity.noContent().build();
    }



}
