package com.flexbox.backend.catalog.box.controller;

import com.flexbox.backend.catalog.box.dto.subscriptionbox.BoxResponse;
import com.flexbox.backend.catalog.box.service.SubscriptionBoxService;
import com.flexbox.backend.common.dto.response.CollectionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/catalog/boxes")
public class SubscriptionBoxController {

    private final SubscriptionBoxService boxService;

    public SubscriptionBoxController(SubscriptionBoxService boxService) {
        this.boxService = boxService;
    }

    @GetMapping
    public ResponseEntity<CollectionResponse<BoxResponse>> getBoxes() {
        return ResponseEntity.ok(boxService.getBoxes());
    }

    @GetMapping("/{boxId}")
    public ResponseEntity<BoxResponse> getBoxById(
            @PathVariable Long boxId) {
        return ResponseEntity.ok(boxService.getBoxById(boxId));
    }

}
