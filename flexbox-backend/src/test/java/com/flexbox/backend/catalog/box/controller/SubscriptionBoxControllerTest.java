package com.flexbox.backend.catalog.box.controller;

import com.flexbox.backend.catalog.box.dto.BoxResponse;
import com.flexbox.backend.catalog.box.service.SubscriptionBoxService;
import com.flexbox.backend.common.dto.response.CollectionResponse;
import com.flexbox.backend.common.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/* This tests the web slice and verifies the HTTP contract of the SubscriptionBoxController
 */
@AutoConfigureMockMvc(addFilters = false) // False as this does not test security
@WebMvcTest(controllers = SubscriptionBoxController.class)
class SubscriptionBoxControllerTest {

    @Autowired
    MockMvcTester mockMvcTester;

    @MockitoBean
    SubscriptionBoxService boxService;

    @Test
    void getBoxes_shouldReturn200_whenBoxesExist() {

        var box1 = new BoxResponse(
                1L,
                "Running Box",
                "A running box",
                "/images/running_box.jpg",
                BigDecimal.valueOf(29.99),
                "CAD",
                true
        );

        var box2 = new BoxResponse(
                2L,
                "Yoga Box",
                "A yoga box",
                "/images/yoga_box.jpg",
                BigDecimal.valueOf(39.99),
                "CAD",
                true
        );

        var expected = new ClassPathResource(
                "responses/catalog/boxes/get-boxes.json"
        );


        when(boxService.getBoxes())
                .thenReturn(new CollectionResponse<>(List.of(box1, box2)));

        assertThat(mockMvcTester
                .get()
                .uri("/api/catalog/boxes"))
                .hasStatusOk()
                .bodyJson()
                .isLenientlyEqualTo(expected);
    }

    @Test
    void getBoxes_shouldReturn404_whenABoxPriceDoesNotExist() {

        when(boxService.getBoxes())
                .thenThrow(new ResourceNotFoundException(
                        "Active price not found for box ID '3'"));


        var result = mockMvcTester
                .get()
                .uri("/api/catalog/boxes")
                .exchange();

        assertThat(result)
                .hasStatus(HttpStatus.NOT_FOUND);
    }

    @Test
    void getBoxById_shouldReturn200_whenBoxExists() {

        Long boxId = 1L;

        var box = new BoxResponse(
                boxId,
                "Running Box",
                "A running box",
                "/images/running_box.jpg",
                BigDecimal.valueOf(29.99),
                "CAD",
                true
        );


        var expected = new ClassPathResource(
                "responses/catalog/boxes/get-box-by-id.json"
        );

        when(boxService.getBoxById(boxId))
                .thenReturn(new BoxResponse(
                        box.id(),
                        box.name(),
                        box.description(),
                        box.imageUrl(),
                        box.price(),
                        box.currency(),
                        box.isActive()
                ));


        assertThat(mockMvcTester
                .get()
                .uri("/api/catalog/boxes/1"))
                .hasStatusOk()
                .bodyJson()
                .isLenientlyEqualTo(expected);
    }


    @Test
    void getBoxById_shouldReturn404_whenBoxNotFound() {

        Long boxId = 10L;

        when(boxService.getBoxById(boxId))
                .thenThrow(new ResourceNotFoundException("Subscription box not found with ID '10'"));

        var result = mockMvcTester
                .get()
                .uri("/api/catalog/boxes/10")
                .exchange();

        assertThat(result)
                .hasStatus(HttpStatus.NOT_FOUND)
                .bodyJson()
                .extractingPath("$.title")
                .isEqualTo("Resource Not Found");

        assertThat(result)
                .bodyJson()
                .extractingPath("$.detail")
                .asString()
                .contains("10");
    }

    @Test
    void getBoxById_shouldReturn404_whenBoxPriceNotFound() {

        Long boxId = 10L;

        when(boxService.getBoxById(boxId))
                .thenThrow(new ResourceNotFoundException("Active price not found for box ID '10'"));

        var result = mockMvcTester
                .get()
                .uri("/api/catalog/boxes/10")
                .exchange();

        assertThat(result)
                .hasStatus(HttpStatus.NOT_FOUND)
                .bodyJson()
                .extractingPath("$.title")
                .isEqualTo("Resource Not Found");

        assertThat(result)
                .bodyJson()
                .extractingPath("$.detail")
                .asString()
                .contains("10");
    }
}
