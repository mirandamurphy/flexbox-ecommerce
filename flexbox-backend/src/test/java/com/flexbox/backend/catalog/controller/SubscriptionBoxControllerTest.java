package com.flexbox.backend.catalog.controller;

import com.flexbox.backend.catalog.box.controller.SubscriptionBoxController;
import com.flexbox.backend.catalog.dto.subscriptionbox.BoxDetailResponse;
import com.flexbox.backend.catalog.dto.subscriptionbox.BoxPriceResponse;
import com.flexbox.backend.catalog.dto.subscriptionbox.BoxProductResponse;
import com.flexbox.backend.catalog.exception.SubscriptionBoxNotFoundException;
import com.flexbox.backend.catalog.box.service.SubscriptionBoxService;
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
    void getAllBoxes_shouldReturn200_andListResponse_whenBoxesExist() {

        when(subscriptionBoxService.getAllSubscriptionBoxes())
                .thenReturn(new SubscriptionBoxListResponse(List.of()));

        var result = mockMvcTester
                .get()
                .uri("/api/subscription-boxes")
                .exchange();

        assertThat(result)
                .hasStatus(HttpStatus.OK);
    }

    @Test
    void getBoxById_shouldReturn200_andBoxDetail_whenBoxExists() {

        var expected = new ClassPathResource(
                "responses/sub-boxes/get-sub-box-by-id.json"
        );

        Long boxId = 1L;

        when(subscriptionBoxService.getSubscriptionBoxById(boxId))
                .thenReturn(new BoxDetailResponse(
                                1L,
                                "Essential Fitness Box",
                                "Entry-level box featuring a mix of protein snacks, hydration products, personal care items, and basic fitness accessories.",
                                "/images/summer-box.jpg",
                                new BoxPriceResponse(
                                        BigDecimal.valueOf(29.99),
                                        "CAD"
                                ), List.of(
                                new BoxProductResponse(
                                        8L,
                                        ,
                                        1
                                ),
                                new BoxProductResponse(
                                        9,
                                        1
                                )
                        ))
                );

        var result = mockMvcTester
                .get()
                .uri("/api/subscription-boxes/1")
                .exchange();

        assertThat(result)
                .hasStatus(HttpStatus.OK)
                .bodyJson()
                .isLenientlyEqualTo(expected);
    }


    @Test
    void getBoxById_shouldReturn404_whenBoxNotFound() {

        Long id = 10L;

        when(subscriptionBoxService.getSubscriptionBoxById(id))
                .thenThrow(new SubscriptionBoxNotFoundException("Subscription box not found with ID: 10"));

        var result = mockMvcTester
                .get()
                .uri("/api/subscription-boxes/10")
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
