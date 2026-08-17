package com.flexbox.backend.admin.catalog.controller;

import com.flexbox.backend.admin.catalog.dto.request.AdminCreateBoxRequest;
import com.flexbox.backend.admin.catalog.dto.request.AdminCreateBoxProductRequest;
import com.flexbox.backend.admin.catalog.dto.response.AdminBoxPriceResponse;
import com.flexbox.backend.admin.catalog.dto.response.AdminBoxProductResponse;
import com.flexbox.backend.admin.catalog.dto.response.AdminBoxResponse;
import com.flexbox.backend.admin.catalog.dto.request.AdminCreateBoxPriceRequest;
import com.flexbox.backend.admin.catalog.service.AdminBoxService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/admin/boxes")
public class AdminBoxController {

    private final AdminBoxService adminBoxService;


    public AdminBoxController(AdminBoxService adminBoxService) {
        this.adminBoxService = adminBoxService;
    }

    @PostMapping
    public ResponseEntity<AdminBoxResponse> createBox(
            @Valid @RequestBody AdminCreateBoxRequest request) {

        var box = adminBoxService.createBox(request);

        var location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{boxId}")
                .buildAndExpand(box.id())
                .toUri();

        return ResponseEntity.created(location).body(box);
    }

    @DeleteMapping("/{boxId}")
    public ResponseEntity<Void> deactivateBoxById(
            @PathVariable Long boxId) {

        adminBoxService.deactivateBox(boxId);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{boxId}/products")
    public ResponseEntity<AdminBoxProductResponse> addBoxProduct (
            @PathVariable Long boxId,
            @Valid @RequestBody AdminCreateBoxProductRequest request) {

        var boxProduct = adminBoxService.createBoxProduct(boxId,request);

        var location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{productId}")
                .buildAndExpand(boxProduct.productId())
                .toUri();

        return ResponseEntity.created(location).body(boxProduct);
    }

    @PostMapping("/{boxId}/prices")
    public ResponseEntity<AdminBoxPriceResponse> setBoxPrice (
            @PathVariable Long boxId,
            @Valid @RequestBody AdminCreateBoxPriceRequest request) {

        var boxPrice = adminBoxService.setBoxPrice(boxId, request);

        var location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{priceId}")
                .buildAndExpand(boxPrice.id())
                .toUri();

        return ResponseEntity.created(location).body(boxPrice);
    }






}
