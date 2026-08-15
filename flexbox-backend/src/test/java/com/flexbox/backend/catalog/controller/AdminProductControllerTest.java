package com.flexbox.backend.catalog.controller;

import com.flexbox.backend.admin.box.controller.AdminProductController;
import com.flexbox.backend.admin.box.dto.response.CategoryResponse;
import com.flexbox.backend.admin.box.dto.response.ProductDetailResponse;
import com.flexbox.backend.admin.box.dto.response.ProductInventoryResponse;
import com.flexbox.backend.admin.box.dto.response.ProductSummaryResponse;
import com.flexbox.backend.admin.box.service.AdminProductService;
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
import static org.mockito.Mockito.when;


@WebMvcTest(controllers = AdminProductController.class)
class AdminProductControllerTest {

    @Autowired
    MockMvcTester mockMvcTester;

    @MockitoBean
    AdminProductService adminProductService;

    @Test
    void getProducts_shouldReturn200_whenProductsExist() {

        var product1 = new ProductSummaryResponse(
                1L,
                "SKU-101",
                "Socks",
                true
        );

        var product2 = new ProductSummaryResponse(
                2L,
                "SKU-102",
                "Sunscreen",
                true
        );

        var expected = new ClassPathResource("responses/products/get-products.json");


        when(adminProductService.getProducts())
                .thenReturn(new CollectionResponse<>(List.of(product1, product2)));


        assertThat(mockMvcTester
                .get()
                .uri("/api/admin/products"))
                .hasStatusOk()
                .bodyJson()
                .isLenientlyEqualTo(expected);
    }

    @Test
    void getProductById_shouldReturn200_whenProductExists() {

        var product = new ProductDetailResponse(
                2L,
                "SKU-102",
                "Sunny",
                "Sunscreen",
                "SPF 60 sunscreen",
                new CategoryResponse(
                        1L, "personal care"
                ),
                true,
                BigDecimal.valueOf(1.01),
                new ProductInventoryResponse(
                        10, 2
                )
        );

        var expected = new ClassPathResource(
                "responses/products/get-product-by-id.json"
        );

        Long productId = 2L;

        when(adminProductService.getProductById(productId))
                .thenReturn(new ProductDetailResponse(
                        product.id(),
                        product.sku(),
                        product.brand(),
                        product.name(),
                        product.description(),
                        new CategoryResponse(product.category().id(), product.category().name()),
                        product.isActive(),
                        product.costPerUnit(),
                        new ProductInventoryResponse(product.inventory().inStock(),
                                product.inventory().reserved()
                        )

                ));


        assertThat(mockMvcTester
                .get()
                .uri("api/admin/products/2"))
                .hasStatusOk()
                .bodyJson()
                .isLenientlyEqualTo(expected);
    }

    @Test
    void getProductById_shouldReturn404_whenProductNotFound() {

        Long productId = 12L;

        when(adminProductService.getProductById(productId))
                .thenThrow(new ResourceNotFoundException("Product not found with ID '%d'".formatted(productId)));

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

        when(adminProductService.getProductById(productId))
                .thenThrow(new ResourceNotFoundException("Product not found with ID '%d'".formatted(productId)));

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


        var result  = mockMvcTester
                .delete()
                .uri("api/admin/products/1")
                .exchange();

        assertThat(result)
                .hasStatus(HttpStatus.NO_CONTENT);

    }


}