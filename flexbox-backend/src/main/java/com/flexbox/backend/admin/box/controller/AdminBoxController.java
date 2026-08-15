package com.flexbox.backend.admin.box.controller;

import com.flexbox.backend.admin.box.dto.request.CreateBoxRequest;
import com.flexbox.backend.admin.box.dto.request.AddProductToBoxRequest;
import com.flexbox.backend.admin.box.dto.response.BoxPriceResponse;
import com.flexbox.backend.admin.box.dto.response.BoxProductResponse;
import com.flexbox.backend.admin.box.dto.response.BoxResponse;
import com.flexbox.backend.admin.box.dto.request.CreateBoxPriceRequest;
import com.flexbox.backend.admin.box.service.AdminBoxService;
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
    public ResponseEntity<BoxResponse> createBox(
            @Valid @RequestBody CreateBoxRequest request) {
        var box = adminBoxService.createBox(request);

        var location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{boxId}")
                .buildAndExpand(box.getId())
                .toUri();

        return ResponseEntity.created(location).body(BoxResponse.from(box));
    }

    @DeleteMapping("/{boxId}")
    public ResponseEntity<Void> deleteBoxById(
            @PathVariable Long boxId) {

        adminBoxService.deactivateBox(boxId);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{boxId}/products")
    public ResponseEntity<BoxProductResponse> addBoxProduct (
            @PathVariable Long boxId,
            @Valid @RequestBody AddProductToBoxRequest request) {

        var boxProduct = adminBoxService.createBoxProduct(boxId,request);
        var location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{productId}")
                .buildAndExpand(boxProduct.getProduct().getId())
                .toUri();

        return ResponseEntity.created(location).body(BoxProductResponse.from(boxProduct));
    }

    @PostMapping("/{boxId}/prices")
    public ResponseEntity<BoxPriceResponse> setBoxPrice (
            @PathVariable Long boxId,
            @Valid @RequestBody CreateBoxPriceRequest request) {

        var boxPrice = adminBoxService.setBoxPrice(boxId, request);

        var location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{priceId}")
                .buildAndExpand(boxPrice.getId())
                .toUri();

        return ResponseEntity.created(location).body(BoxPriceResponse.from(boxPrice));
    }






}
