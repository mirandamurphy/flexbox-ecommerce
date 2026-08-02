package com.flexbox.backend.catalog.controller;

import com.flexbox.backend.catalog.dto.subscriptionbox.SubscriptionBoxDetail;
import com.flexbox.backend.catalog.response.SubscriptionBoxListResponse;
import com.flexbox.backend.catalog.service.SubscriptionBoxService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/subscription-boxes")
public class SubscriptionBoxController {

    private final SubscriptionBoxService subscriptionBoxService;

    public SubscriptionBoxController(SubscriptionBoxService subscriptionBoxService) {
        this.subscriptionBoxService = subscriptionBoxService;
    }

    @GetMapping
    public ResponseEntity<SubscriptionBoxListResponse> getAllSubscriptionBoxes() {
        var subscriptionBoxes = subscriptionBoxService.getAllSubscriptionBoxes();
        return ResponseEntity.ok(subscriptionBoxes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionBoxDetail> getSubscriptionBoxById(
            @PathVariable Long id) {
        var subscriptionBox = subscriptionBoxService.getSubscriptionBoxById(id);
        return ResponseEntity.ok(subscriptionBox);
    }


}
