package com.flexbox.backend.catalog.controller;

import com.flexbox.backend.catalog.dto.subscriptionbox.SubscriptionBoxDetail;
import com.flexbox.backend.catalog.dto.subscriptionbox.SubscriptionBoxPriceSummary;
import com.flexbox.backend.catalog.dto.subscriptionbox.SubscriptionBoxProductSummary;
import com.flexbox.backend.catalog.exception.SubscriptionBoxNotFoundException;
import com.flexbox.backend.catalog.response.SubscriptionBoxListResponse;
import com.flexbox.backend.catalog.service.SubscriptionBoxService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@WebMvcTest(controllers = SubscriptionBoxController.class)
class SubscriptionBoxControllerTest {

    @Autowired
    MockMvcTester mockMvcTester;

    @MockitoBean
    SubscriptionBoxService subscriptionBoxService;

    @Test
    void getAllSubscriptionBoxes_shouldReturn200_andListResponse_whenBoxesExist() {

        when(subscriptionBoxService.getAllSubscriptionBoxes())
                .thenReturn(new SubscriptionBoxListResponse(List.of()));

        var result = mockMvcTester
                .get()
                .uri("/subscription-boxes")
                .exchange();

        assertThat(result)
                .hasStatus(HttpStatus.OK);
    }

    @Test
    void getSubscriptionBoxById_shouldReturn200_andBoxDetail_whenBoxExists() {

        var expected = new ClassPathResource(
                "responses/sub-boxes/get-sub-box-by-id.json"
        );

        Long boxId = 1L;

        when(subscriptionBoxService.getSubscriptionBoxById(boxId))
                .thenReturn(new SubscriptionBoxDetail(
                                1L,
                                "Essential Fitness Box",
                                "Entry-level box featuring a mix of protein snacks, hydration products, personal care items, and basic fitness accessories.",
                                new SubscriptionBoxPriceSummary(
                                        BigDecimal.valueOf(29.99),
                                        "CAD"
                                ), List.of(
                                new SubscriptionBoxProductSummary(
                                        8L,
                                        "PB Protein Crunch Bar",
                                        "PowerBar Pro",
                                        1
                                ),
                                new SubscriptionBoxProductSummary(
                                        9L,
                                        "Protein Cookie",
                                        "ActiveLife",
                                        1
                                )
                        ))
                );

        var result = mockMvcTester
                .get()
                .uri("/subscription-boxes/1")
                .exchange();

        assertThat(result)
                .hasStatus(HttpStatus.OK)
                .bodyJson()
                .isLenientlyEqualTo(expected);
    }


    @Test
    void getSubscriptionBoxById_shouldReturn404_whenBoxNotFound() {

        var expected = new ClassPathResource("responses/sub-boxes/get-sub-box-with-id-not-found.json");

        Long id = 10L;

        when(subscriptionBoxService.getSubscriptionBoxById(id))
                .thenThrow(new SubscriptionBoxNotFoundException("Subscription box not found with ID: 10"));

        var result = mockMvcTester
                .get()
                .uri("/subscription-boxes/10")
                .exchange();

        assertThat(result)
                .hasStatus(HttpStatus.NOT_FOUND)
                .bodyJson()
                .extractingPath("$.title")
                .isEqualTo("Subscription Box Not Found");

        assertThat(result)
                .bodyJson()
                .extractingPath("$.detail")
                .isEqualTo("Subscription box not found with ID: 10");

        assertThat(result)
                .bodyJson()
                .extractingPath("$.timestamp")
                .isNotNull();
    }
    }
