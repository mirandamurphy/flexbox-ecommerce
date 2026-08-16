package com.flexbox.backend.admin.analytics.service;

import com.flexbox.backend.admin.analytics.dto.MonthlySalesResponse;
import com.flexbox.backend.admin.analytics.dto.SubscriptionBoxCostResponse;
import com.flexbox.backend.admin.analytics.dto.SubscriptionBoxProductCostResponse;
import com.flexbox.backend.admin.analytics.model.*;
import com.flexbox.backend.admin.analytics.repository.MonthlySalesRepository;
import com.flexbox.backend.admin.analytics.repository.SubscriptionBoxCostRepository;
import com.flexbox.backend.admin.analytics.repository.SubscriptionBoxProductCostRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AdminAnalyticsServiceTest {

    @Mock
    MonthlySalesRepository salesRepository;

    @Mock
    SubscriptionBoxProductCostRepository boxProductCostRepository;

    @Mock
    SubscriptionBoxCostRepository boxCostRepository;

    @InjectMocks
    AdminAnalyticsService analyticsService;

    @Test
    void getBoxCosts() {

        Long runningBoxId = 1L;
        Long yogaBoxId = 2L;

        var runningBox = boxCost(runningBoxId, "Running Box", "12.99");
        var yogaBox = boxCost(yogaBoxId, "Yoga Box", "14.00");

        given(boxCostRepository.findAll())
                .willReturn(List.of(runningBox, yogaBox));

        var result = analyticsService.getBoxCosts();

        assertThat(result.items())
                .hasSize(2);

        assertThat(result.items()).extracting(
                SubscriptionBoxCostResponse::subscriptionBoxId)
                        .containsExactlyInAnyOrder(1L, 2L);

        assertThat(result.items()).extracting(
                        SubscriptionBoxCostResponse::boxName)
                .containsExactlyInAnyOrder("Running Box", "Yoga Box");

        assertThat(result.items()).extracting(
                        SubscriptionBoxCostResponse::boxCost)
                .containsExactlyInAnyOrder(
                        new BigDecimal("12.99"),
                        new BigDecimal("14.00")
                );


        verify(boxCostRepository).findAll();
    }

    @Test
    void getBoxProductCosts_shouldReturnMappedProductCosts() {

        Long runningBoxId = 1L;
        Long yogaBoxId = 2L;

        var sunscreen = productCost(
                runningBoxId,
                1L,
                "Running Box",
                "Sunny",
                "Sunscreen",
                1L,
                "personal care",
                2,
                "1.02"
        );

        var socks = productCost(
                yogaBoxId,
                2L,
                "Running Box",
                "Comfy",
                "Socks",
                2L,
                "appeal",
                1,
                "1.45"
        );

        given(boxProductCostRepository.findAll())
                .willReturn(List.of(sunscreen, socks));

        var result = analyticsService.getBoxProductCosts();

        assertThat(result.items()).hasSize(2);

        assertThat(result.items())
                .extracting(SubscriptionBoxProductCostResponse::subscriptionBoxId)
                .containsExactlyInAnyOrder(1L, 2L);

        assertThat(result.items())
                .extracting(SubscriptionBoxProductCostResponse::productId)
                .containsExactlyInAnyOrder(1L, 2L);

        assertThat(result.items())
                .extracting(SubscriptionBoxProductCostResponse::productName)
                .containsExactlyInAnyOrder("Sunscreen", "Socks");

        assertThat(result.items())
                .extracting(SubscriptionBoxProductCostResponse::productCost)
                .containsExactlyInAnyOrder(
                        new BigDecimal("1.45"),
                        new BigDecimal("1.02"));

        assertThat(result.items())
                .extracting(SubscriptionBoxProductCostResponse::quantity)
                .containsExactlyInAnyOrder(1, 2);


        verify(boxProductCostRepository).findAll();

    }

    @Test
    void getBoxProductCostByBoxId_shouldReturnProductsForRequestBox() {

        Long boxId = 1L;

        var sunscreen = productCost(
                boxId,
                1L,
                "Running Box",
                "Sunny",
                "Sunscreen",
                1L,
                "personal care",
                2,
                "1.02"
        );

        var socks = productCost(
                boxId,
                2L,
                "Running Box",
                "Comfy",
                "Socks",
                2L,
                "appeal",
                1,
                "1.45"
        );

       given(boxProductCostRepository.findById_SubscriptionBoxId(boxId))
                .willReturn(List.of(sunscreen,socks));

        var result = analyticsService.getBoxProductCostByBoxId(boxId);

        assertThat(result.items()).hasSize(2);

        assertThat(result.items())
                .extracting(SubscriptionBoxProductCostResponse::subscriptionBoxId)
                        .containsExactly(1L, 1L);

        assertThat(result.items())
                .extracting(SubscriptionBoxProductCostResponse::productId)
                .containsExactlyInAnyOrder(1L, 2L);

        assertThat(result.items())
                .extracting(SubscriptionBoxProductCostResponse::productName)
                .containsExactlyInAnyOrder("Sunscreen", "Socks");

        assertThat(result.items())
                .extracting(SubscriptionBoxProductCostResponse::productCost)
                .containsExactlyInAnyOrder(
                        new BigDecimal("1.45"),
                        new BigDecimal("1.02"));

        assertThat(result.items())
                .extracting(SubscriptionBoxProductCostResponse::quantity)
                .containsExactlyInAnyOrder(1, 2);

        verify(boxProductCostRepository).findById_SubscriptionBoxId(boxId);
    }

    @Test
    void getMonthlySales_shouldReturnMappedMonthlySales() {

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

        var julyRunningBox = monthlySales(
                july,
                1L,
                "Running Box",
                123L,
                "5642.32",
                "2342.32",
                "3223.32"
        );

        var julyYogaBox = monthlySales(
                july,
                2L,
                "Yoga Box",
                134L,
                "7483.52",
                "454.32",
                "1234.65"
        );

        var augustRunningBox = monthlySales(
                august,
                1L,
                "Running Box",
                110L,
                "3442.32",
                "1242.32",
                "1333.32"
        );

        var augustYogaBox = monthlySales(
                august,
                2L,
                "Yoga Box",
                30L,
                "442.32",
                "232.32",
                "533.32"
        );

        given(salesRepository.findAll())
                .willReturn(List.of(
                        julyRunningBox,
                        julyYogaBox,
                        augustRunningBox,
                        augustYogaBox));

        var result = analyticsService.getMonthlySales();


        assertThat(result.items()).hasSize(4);

        assertThat(result.items())
                .extracting(MonthlySalesResponse::subscriptionBoxId)
                .containsExactlyInAnyOrder(1L, 1L, 2L, 2L);

        assertThat(result.items())
                .extracting(MonthlySalesResponse::unitsSold)
                .containsExactlyInAnyOrder(123L, 134L, 110L, 30L);

        assertThat(result.items())
                .extracting(MonthlySalesResponse::grossRevenue)
                .containsExactlyInAnyOrder(
                        new BigDecimal("5642.32"),
                        new BigDecimal("7483.52"),
                        new BigDecimal("3442.32"),
                        new BigDecimal("442.32")
                );

        verify(salesRepository).findAll();
    }


    private SubscriptionBoxProductCost productCost(
            Long boxId,
            Long productId,
            String boxName,
            String brand,
            String productName,
            Long categoryId,
            String categoryName,
            Integer quantity,
            String  productCost) {

        return new SubscriptionBoxProductCost(
                new SubscriptionBoxProductCostId(boxId, productId),
                boxName,
                brand,
                productName,
                categoryId,
                categoryName,
                quantity,
                new BigDecimal(productCost));

    }
    private SubscriptionBoxCost boxCost(
            Long boxId,
            String name,
            String cost) {
        return new SubscriptionBoxCost(
                boxId,
                name,
                new BigDecimal(cost)
        );
    }

    private MonthlySales monthlySales(
            OffsetDateTime month,
            Long boxId,
            String boxName,
            Long unitsSold,
            String grossRevenue,
            String productCost,
            String grossProfit) {

        return new MonthlySales(
                new MonthlySalesId(month, boxId),
                boxName,
                unitsSold,
                new BigDecimal(grossRevenue),
                new BigDecimal(productCost),
                new BigDecimal(grossProfit)
        );
    }




}