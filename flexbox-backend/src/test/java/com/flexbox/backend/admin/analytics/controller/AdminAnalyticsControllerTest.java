package com.flexbox.backend.admin.analytics.controller;

import com.flexbox.backend.admin.analytics.dto.MonthlySalesResponse;
import com.flexbox.backend.admin.analytics.dto.SubscriptionBoxCostResponse;
import com.flexbox.backend.admin.analytics.dto.SubscriptionBoxProductCostResponse;
import com.flexbox.backend.admin.analytics.service.AdminAnalyticsService;
import com.flexbox.backend.common.dto.response.CollectionResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@WebMvcTest(controllers = AdminAnalyticsController.class)
class AdminAnalyticsControllerTest {

    @Autowired
    MockMvcTester mockMvcTester;

    @MockitoBean
    AdminAnalyticsService adminAnalyticsService;


    @Test
    void getAllBoxCosts_shouldReturn200() {


        var runningBox = new SubscriptionBoxCostResponse(
                1L,
                "Running Box",
                new BigDecimal("12.99")
        );

        var yogaBox = new SubscriptionBoxCostResponse(
                2L,
                "Yoga Box",
                new BigDecimal("14.00")
        );

        var response = new CollectionResponse<>(List.of(runningBox, yogaBox));

        var expected = new ClassPathResource("responses/admin/box-costs-response.json");

        given(adminAnalyticsService.getBoxCosts())
                .willReturn(response);

       assertThat(mockMvcTester
                .get()
                .uri("/api/admin/analytics/boxes"))
               .hasStatusOk()
               .bodyJson()
               .isLenientlyEqualTo(expected);

       then(adminAnalyticsService)
               .should()
               .getBoxCosts();
    }

    @Test
    void getAllBoxProductCosts_shouldReturn200() {

        var runningBox = new SubscriptionBoxProductCostResponse(
                1L,
                1L,
                "Running Box",
                "Sunny",
                "Sunscreen",
                1L,
                "personal care",
                1,
                new BigDecimal("1.02")
        );

        var yogaBox = new SubscriptionBoxProductCostResponse(
                2L,
                1L,
                "Yoga Box",
                "Sunny",
                "Sunscreen",
                1L,
                "personal care",
                3,
                new BigDecimal("1.02")
        );

        var response = new CollectionResponse<>(
                List.of(runningBox, yogaBox));

        var expected = new ClassPathResource("responses/admin/all-box-product-costs-response.json");

        given(adminAnalyticsService.getBoxProductCosts())
                .willReturn(response);

        assertThat(mockMvcTester
                .get()
                .uri("/api/admin/analytics/boxes/products"))
                .hasStatusOk()
                .bodyJson()
                .isLenientlyEqualTo(expected);

        then(adminAnalyticsService)
                .should()
                .getBoxProductCosts();
    }

    @Test
    void getBoxProductCostByBoxId_shouldReturn200() {

        Long boxId = 1L;

        var suncreen = new SubscriptionBoxProductCostResponse(
                boxId,
                1L,
                "Running Box",
                "Sunny",
                "Sunscreen",
                1L,
                "personal care",
                1,
                new BigDecimal("1.02")
        );

        var socks = new SubscriptionBoxProductCostResponse(
                boxId,
                2L,
                "Running Box",
                "Comfy",
                "Socks",
                2L,
                "apparel",
                2,
                new BigDecimal("1.56")

        );

        var response = new CollectionResponse<>(
                List.of(suncreen, socks));

        var expected = new ClassPathResource("responses/admin/box-product-costs-response.json");

        given(adminAnalyticsService.getBoxProductCostByBoxId(boxId))
                .willReturn(response);

        assertThat(mockMvcTester
                .get()
                .uri("/api/admin/analytics/boxes/1/products"))
                .hasStatusOk()
                .bodyJson()
                .isLenientlyEqualTo(expected);

        then(adminAnalyticsService)
                .should()
                .getBoxProductCostByBoxId(boxId);
    }

    @Test
    void getAllMonthlySales_shouldReturn200() {

        OffsetDateTime july = OffsetDateTime.of(
                2026,
                7,
                2,
                23,
                38,
                39,
                657_650_000,
                ZoneOffset.UTC
        );

        OffsetDateTime august = OffsetDateTime.of(
                2026,
                8,
                2,
                23,
                38,
                39,
                657_650_000,
                ZoneOffset.UTC
        );


        var julySales = new MonthlySalesResponse(
                july,
                1L,
                "Running Box",
                123L,
                new BigDecimal("5642.32"),
                new BigDecimal("2342.32"),
                new BigDecimal("3223.30")
        );

        var augustSales = new MonthlySalesResponse(
                august,
                1L,
                "Running Box",
                234L,
                new BigDecimal("9944.33"),
                new BigDecimal("4422.42"),
                new BigDecimal("2312.32")
        );

        var response = new CollectionResponse<>(
                List.of(julySales, augustSales
                ));

        var expected = new ClassPathResource("responses/admin/monthly-sales-response.json");

        given(adminAnalyticsService.getMonthlySales())
                .willReturn(response);

        assertThat(mockMvcTester
                .get()
                .uri("/api/admin/analytics/sales"))
                .hasStatusOk()
                .bodyJson()
                .isLenientlyEqualTo(expected);

        then(adminAnalyticsService)
                .should()
                .getMonthlySales();
    }
}