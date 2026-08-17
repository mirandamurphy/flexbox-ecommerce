package com.flexbox.backend.admin.catalog.controller;

import com.flexbox.backend.admin.catalog.dto.request.AdminCreateBoxProductRequest;
import com.flexbox.backend.admin.catalog.dto.request.AdminCreateBoxPriceRequest;
import com.flexbox.backend.admin.catalog.dto.request.AdminCreateBoxRequest;
import com.flexbox.backend.admin.catalog.dto.response.AdminBoxPriceResponse;
import com.flexbox.backend.admin.catalog.dto.response.AdminBoxProductResponse;
import com.flexbox.backend.admin.catalog.dto.response.AdminBoxResponse;
import com.flexbox.backend.admin.catalog.service.AdminBoxService;
import com.flexbox.backend.common.exception.BusinessRuleException;
import com.flexbox.backend.common.exception.ResourceAlreadyExistsException;
import com.flexbox.backend.common.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

/*
Slice test of AdminBoxController that tests
- URL routing
- JSON request body translates correctly to request DTO
- DTO response body translates correctly to JSON
- Proper HTTP status codes are returned based on validations, exceptions, and success.
- Path variables are correctly passed
- Controller correctly interacts with the AdminBoxService, passing the correct arguments
 */

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = AdminBoxController.class)
class AdminBoxControllerTest {

    @Autowired
    MockMvcTester mockMvcTester;

    @MockitoBean
    AdminBoxService boxService;

    @Test
    void createBox_shouldReturn201_ifBoxCreated() {

        String requestBody = """
                             {
                               "name": "Yoga Box",
                               "description": "Monthly yoga fitness box",
                               "imagePath": "/images/yoga_box.jpg",
                               "availableUnits": 10,
                               "isActive": true
                             }
                             """;

        var request = new AdminCreateBoxRequest(
                "Yoga Box",
                "Monthly yoga fitness box",
                "/images/yoga_box.jpg",
                10,
                true
        );

        var response = new AdminBoxResponse(
                1L,
                "Yoga Box",
                "Monthly yoga fitness box",
                "/images/yoga_box.jpg",
                10,
                true
        );

        var expected = new ClassPathResource("responses/catalog/boxes/create-box-response.json");

        given(boxService.createBox(request))
                .willReturn(response);


        assertThat(mockMvcTester
                .post()
                .uri("/api/admin/boxes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .hasStatus(HttpStatus.CREATED)
                .bodyJson()
                .isLenientlyEqualTo(expected);

        then(boxService)
                .should()
                .createBox(request);

    }

    @Test
    void createBox_shouldReturn409_ifBoxNameAlreadyExists() {

        String requestBody = """
                             {
                               "name": "Yoga Box",
                               "description": "Monthly yoga fitness box",
                               "imagePath": "/images/yoga_box.jpg",
                               "availableUnits": 10,
                               "isActive": true
                             }
                             """;

        var request = new AdminCreateBoxRequest(
                "Yoga Box",
                "Monthly yoga fitness box",
                "/images/yoga_box.jpg",
                10,
                true
        );


        given(boxService.createBox(request))
                .willThrow(new ResourceAlreadyExistsException(
                        "A subscription box with the name 'Yoga Box' already exists."
                ));


        var result = mockMvcTester
                .post()
                .uri("/api/admin/boxes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .exchange();

        assertThat(result)
                .hasStatus(HttpStatus.CONFLICT);

    }

    @Test
    void deactivateBoxById_shouldReturn204_ifBoxDeactivated() {
        Long boxId = 1L;

        assertThat( mockMvcTester
                .delete()
                .uri("/api/admin/boxes/1"))
               .hasStatus(HttpStatus.NO_CONTENT);

        then(boxService)
                .should()
                .deactivateBox(boxId);
    }

    @Test
    void deactivateBoxById_shouldReturn404_ifBoxNotFound() {

        Long boxId = 1L;

        willThrow(new ResourceNotFoundException(
                "Subscription box for id '%d' not found.".formatted(boxId)
        ))
                .given(boxService)
                .deactivateBox(boxId);

        var result = mockMvcTester
                .delete()
                .uri("/api/admin/boxes/1")
                .exchange();

        assertThat(result)
                .hasStatus(HttpStatus.NOT_FOUND);

        then(boxService)
                .should()
                .deactivateBox(boxId);
    }

    @Test
    void addBoxProduct_shouldReturn201_ifBoxProductAdded() {

        Long boxId = 1L;

        String requestBody = """
                             {
                               "productId": 1,
                               "quantity": 3
                             }
                             """;

        var request = new AdminCreateBoxProductRequest(
                1L,
                3
        );

        var response = new AdminBoxProductResponse(
                1L,
                1L,
                "Sunscreen",
                3
        );


        var expected = new ClassPathResource("responses/catalog/boxes/create-box-product-response.json");

        given(boxService.createBoxProduct(boxId, request))
                .willReturn(response);

        assertThat( mockMvcTester
                .post()
                .uri("/api/admin/boxes/1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .hasStatus(HttpStatus.CREATED)
                .bodyJson()
                .isLenientlyEqualTo(expected);

        then(boxService)
                .should()
                .createBoxProduct(boxId, request);

    }

    @Test
    void addBoxProduct_shouldReturn404_ifBoxNotFound() {

        Long boxId = 1L;

        String requestBody = """
                             {
                               "productId": 1,
                               "quantity": 3
                             }
                             """;

        var request = new AdminCreateBoxProductRequest(
                1L,
                3
        );

        given(boxService.createBoxProduct(boxId, request))
                .willThrow(new ResourceNotFoundException(
                        "Subscription box for id '%d' not found.".formatted(boxId)
                ));

        var result = mockMvcTester
                .post()
                .uri("/api/admin/boxes/1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .exchange();

        assertThat(result)
                .hasStatus(HttpStatus.NOT_FOUND);
    }

    @Test
    void addBoxProduct_shouldReturn404_ifProductNotFound() {

        Long boxId = 1L;

        String requestBody = """
                             {
                               "productId": 1,
                               "quantity": 3
                             }
                             """;

        var request = new AdminCreateBoxProductRequest(
                1L,
                3
        );

        given(boxService.createBoxProduct(boxId, request))
                .willThrow(new ResourceNotFoundException(
                        "Product for id '%d' not found.".formatted(request.productId())
                ));

        var result = mockMvcTester
                .post()
                .uri("/api/admin/boxes/1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .exchange();

        assertThat(result)
                .hasStatus(HttpStatus.NOT_FOUND);
    }

    @Test
    void addBoxProduct_shouldReturn400_ifProductIsInactive() {

        Long boxId = 1L;

        String requestBody = """
                             {
                               "productId": 1,
                               "quantity": 3
                             }
                             """;

        var request = new AdminCreateBoxProductRequest(
                1L,
                3
        );

        given(boxService.createBoxProduct(boxId, request))
                .willThrow(new BusinessRuleException(
                        "Cannot add an inactive product to a subscription box.")
                );

        var result = mockMvcTester
                .post()
                .uri("/api/admin/boxes/1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .exchange();

        assertThat(result)
                .hasStatus(HttpStatus.BAD_REQUEST);
    }

    @Test
    void addBoxProduct_shouldReturn409_ifProductIsAlreadyInBox() {

        Long boxId = 1L;

        String requestBody = """
                             {
                               "productId": 1,
                               "quantity": 3
                             }
                             """;

        var request = new AdminCreateBoxProductRequest(
                1L,
                3
        );

        given(boxService.createBoxProduct(boxId, request))
                .willThrow(new ResourceAlreadyExistsException(
                        "Product with id '%d' is already included in this subscription box".formatted(request.productId())
                ));

        var result = mockMvcTester
                .post()
                .uri("/api/admin/boxes/1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .exchange();

        assertThat(result)
                .hasStatus(HttpStatus.CONFLICT);
    }

    @Test
    void setBoxPrice_shouldReturn201_ifBoxPriceCreated() {

        Long boxId = 1L;

        var startsAt = OffsetDateTime.of(
                2026, 8, 30,
                0, 0, 0, 0,
                ZoneOffset.UTC
        );

        var endsAt = OffsetDateTime.of(
                2026, 9, 15,
                0, 0, 0, 0,
                ZoneOffset.UTC
        );

        String requestBody = """
                             {
                               "price": 49.99,
                               "startsAt": "2026-08-30T00:00:00Z",
                               "endsAt": "2026-09-15T00:00:00Z"
                             }
                             """;

        var request = new AdminCreateBoxPriceRequest(
                new BigDecimal("49.99"),
                startsAt,
                endsAt
        );

        var response = new AdminBoxPriceResponse(
                1L,
                1L,
                new BigDecimal("49.99"),
                startsAt,
                endsAt
        );

        given(boxService.setBoxPrice(boxId, request))
                .willReturn(response);

        var expected = new ClassPathResource("responses/catalog/boxes/create-box-price-response.json");

        assertThat(mockMvcTester
                .post()
                .uri("/api/admin/boxes/1/prices")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .hasStatus(HttpStatus.CREATED)
                .bodyJson()
                .isLenientlyEqualTo(expected);

        then(boxService)
                .should()
                .setBoxPrice(boxId, request);
    }

    @Test
    void setBoxPrice_shouldReturn404_ifBoxNotFound() {

        Long boxId = 10L;

        var startsAt = OffsetDateTime.of(
                2026, 8, 30,
                0, 0, 0, 0,
                ZoneOffset.UTC
        );

        var endsAt = OffsetDateTime.of(
                2026, 9, 15,
                0, 0, 0, 0,
                ZoneOffset.UTC
        );

        String requestBody = """
                             {
                               "price": 49.99,
                               "startsAt": "2026-08-30T00:00:00Z",
                               "endsAt": "2026-09-15T00:00:00Z"
                             }
                             """;

        var request = new AdminCreateBoxPriceRequest(
                new BigDecimal("49.99"),
                startsAt,
                endsAt
        );

        given(boxService.setBoxPrice(boxId, request))
                .willThrow(new ResourceNotFoundException("Subscription box for id '%d' not found.".formatted(boxId)));

        var result = mockMvcTester
                .post()
                .uri("/api/admin/boxes/10/prices")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .exchange();

        assertThat(result)
                .hasStatus(HttpStatus.NOT_FOUND);
    }

}