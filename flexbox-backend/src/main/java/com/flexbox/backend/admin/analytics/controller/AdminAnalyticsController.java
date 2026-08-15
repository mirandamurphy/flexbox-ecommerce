package com.flexbox.backend.admin.analytics.controller;

import com.flexbox.backend.admin.analytics.service.AdminAnalyticsService;
import com.flexbox.backend.admin.analytics.dto.MonthlySalesResponse;
import com.flexbox.backend.admin.analytics.dto.SubscriptionBoxCostResponse;
import com.flexbox.backend.admin.analytics.dto.SubscriptionBoxProductCostResponse;
import com.flexbox.backend.common.dto.response.CollectionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/admin/analytics")
public class AdminAnalyticsController {

    private final AdminAnalyticsService adminAnalyticsService;


    public AdminAnalyticsController(AdminAnalyticsService adminAnalyticsService) {
        this.adminAnalyticsService = adminAnalyticsService;
    }

    @GetMapping("/boxes")
    public ResponseEntity<CollectionResponse<SubscriptionBoxCostResponse>> getAllBoxCosts() {
        return ResponseEntity.ok(adminAnalyticsService.getBoxCosts());
    }

    @GetMapping("/boxes/products")
    public ResponseEntity<CollectionResponse<SubscriptionBoxProductCostResponse>> getAllBoxProductCosts() {
        return ResponseEntity.ok(adminAnalyticsService.getBoxProductCosts());
    }


    @GetMapping("/boxes/{boxId}/products")
    public ResponseEntity<CollectionResponse<SubscriptionBoxProductCostResponse>> getBoxProductCostByBoxId (
            @PathVariable Long boxId) {
        return ResponseEntity.ok(adminAnalyticsService.getBoxProductCostByBoxId(boxId));
    }


    @GetMapping("/sales")
    public ResponseEntity<CollectionResponse<MonthlySalesResponse>> getAllMonthlySales() {
        return ResponseEntity.ok(adminAnalyticsService.getMonthlySales());
    }

}
