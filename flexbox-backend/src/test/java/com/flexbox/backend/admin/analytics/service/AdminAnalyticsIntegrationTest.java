package com.flexbox.backend.admin.analytics.service;

import com.flexbox.backend.TestcontainersConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Import(TestcontainersConfiguration.class)
class AdminAnalyticsIntegrationTest {

    @Autowired
    AdminAnalyticsService adminAnalyticsService;

    @Test
    void getBoxCosts_shouldReturnData() {
        var result = adminAnalyticsService.getBoxCosts();
        assertThat(result.items()).isNotEmpty();
    }

    @Test
    void getBoxProductCosts_shouldReturnData() {
        var result = adminAnalyticsService.getBoxProductCosts();
        assertThat(result.items()).isNotEmpty();
    }

    @Test
    void getBoxProductCostByBoxId_shouldReturnData() {
        var result = adminAnalyticsService.getBoxProductCostByBoxId(1L);
        assertThat(result.items()).isNotEmpty();
    }

    @Test
    void getMonthlySales_shouldReturnData() {
        var result = adminAnalyticsService.getMonthlySales();
        assertThat(result.items()).isNotEmpty();
    }
}