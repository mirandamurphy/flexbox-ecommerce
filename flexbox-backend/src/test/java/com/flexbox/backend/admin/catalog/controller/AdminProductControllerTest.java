package com.flexbox.backend.admin.catalog.controller;

import com.flexbox.backend.admin.catalog.dto.response.AdminCategoryResponse;
import com.flexbox.backend.admin.catalog.dto.response.AdminProductDetailResponse;
import com.flexbox.backend.admin.catalog.dto.response.AdminProductInventoryResponse;
import com.flexbox.backend.admin.catalog.dto.response.AdminProductSummaryResponse;
import com.flexbox.backend.admin.catalog.service.AdminProductService;
import com.flexbox.backend.common.dto.response.CollectionResponse;
import com.flexbox.backend.common.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.core.io.ClassPathResource;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;


@WebMvcTest(controllers = AdminProductController.class)
class AdminProductControllerTest {

    @Autowired
    MockMvcTester mockMvcTester;

    @MockitoBean
    AdminProductService adminProductService;

    @Test
    void getProducts_shouldReturn200_whenProductsExist() {

        var socks = new AdminProductSummaryResponse(
                1L,
                "SKU-101",
                "Socks",
                true
        );

        var sunscreen = new AdminProductSummaryResponse(
                2L,
                "SKU-102",
                "Sunscreen",
                true
        );

        var expected = new ClassPathResource("responses/catalog/products/get-products.json");


        given(adminProductService.getProducts())
                .willReturn(new CollectionResponse<>(List.of(socks, sunscreen)));


        assertThat(mockMvcTester
                .get()
                .uri("/api/admin/products"))
                .hasStatusOk()
                .bodyJson()
                .isLenientlyEqualTo(expected);
    }

    @Test
    void getProductById_shouldReturn200_whenProductExists() {

        var sunscreen = new AdminProductDetailResponse(
                2L,
                "SKU-102",
                "Sunny",
                "Sunscreen",
                "SPF 60 sunscreen",
                new AdminCategoryResponse(
                        1L, "personal care"
                ),
                true,
                BigDecimal.valueOf(1.01),
                new AdminProductInventoryResponse(
                        10, 2
                )
        );

        var expected = new ClassPathResource(
                "responses/catalog/products/get-product-by-id.json"
        );

        Long productId = 2L;

        given(adminProductService.getProductById(productId))
                .willReturn(new AdminProductDetailResponse(
                        sunscreen.id(),
                        sunscreen.sku(),
                        sunscreen.brand(),
                        sunscreen.name(),
                        sunscreen.description(),
                        new AdminCategoryResponse(sunscreen.category().id(), sunscreen.category().name()),
                        sunscreen.isActive(),
                        sunscreen.costPerUnit(),
                        new AdminProductInventoryResponse(sunscreen.inventory().inStock(),
                                sunscreen.inventory().reserved()
                        )

                ));


        assertThat(
                mockMvcTester
                .get()
                .uri("/api/admin/products/2"))
                .hasStatusOk()
                .bodyJson()
                .isLenientlyEqualTo(expected);
    }

    @Test
    void getProductById_shouldReturn404_whenProductNotFound() {

        Long productId = 12L;

        given(adminProductService.getProductById(productId))
                .willThrow(
                        new ResourceNotFoundException(
                                "Product not found with ID '%d'".formatted(productId)));

        var result = mockMvcTester
                .get()
                .uri("/api/admin/products/12")
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
                .contains("12");
    }

    @Test
    void getProductById_shouldReturn404_andIncludeIdInFailMessage_whenProductNotFound() {

        Long productId = 12L;

        given(adminProductService.getProductById(productId))
                .willThrow(new ResourceNotFoundException("Product not found with ID '%d'".formatted(productId)));

        var result = mockMvcTester
                .get()
                .uri("/api/admin/products/12")
                .exchange();

        assertThat(result)
                .hasFailed()
                .failure()
                .hasMessage("Product not found with ID '12'");

    }

    @Test
    void deactivateProductById_shouldReturnNoContent_onSuccess() {
        Long productId = 1L;

        var result  = mockMvcTester
                .delete()
                .uri("/api/admin/products/1")
                .exchange();

        assertThat(result)
                .hasStatus(HttpStatus.NO_CONTENT);

        then(adminProductService)
                .should()
                .deactivateProductById(productId);

    }


}