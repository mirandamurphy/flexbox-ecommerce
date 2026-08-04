package com.flexbox.backend.catalog.controller;

import com.flexbox.backend.catalog.dto.category.CategorySummary;
import com.flexbox.backend.catalog.dto.product.ProductDetail;
import com.flexbox.backend.catalog.exception.ProductNotFoundException;
import com.flexbox.backend.catalog.response.ProductListResponse;
import com.flexbox.backend.catalog.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;
import org.springframework.core.io.ClassPathResource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@WebMvcTest(controllers = ProductController.class)
class ProductControllerTest {

    @Autowired
    MockMvcTester mockMvcTester;

    @MockitoBean
    ProductService productService;

    @Test
    void getAllProducts_shouldReturn200_AndProductListResponse_whenProductsExist() {

        when(productService.getAllProducts())
                .thenReturn(new ProductListResponse(List.of()));

        var result = mockMvcTester
                .get()
                .uri("/api/products")
                .exchange();

        assertThat(result)
                .hasStatus(HttpStatus.OK);
    }

    @Test
    void getProductById_shouldReturn200_AndProductDetail_whenProductExists() {

        var expected = new ClassPathResource(
                "responses/products/get-product-by-id-response.json"
        );

        Long productId = 4L;
        Long categoryId = 1L;

        when(productService.getProductById(productId))
                .thenReturn(new ProductDetail(
                        4L,
                        "Chocolate Whey Protein Packet",
                        "FitFuel",
                        "Single-serve whey protein powder, 25g protein",
                        new CategorySummary(
                                categoryId,
                                "snacks"),
                        true
                        )
                );

        var result = mockMvcTester
                .get()
                .uri("/api/products/4")
                .exchange();

        assertThat(result)
                .hasStatus(HttpStatus.OK)
                .bodyJson()
                .isLenientlyEqualTo(expected);
    }

    @Test
    void getProductById_shouldReturn404_whenProductNotFound() {

        Long id = 1L;

        when(productService.getProductById(id))
                .thenThrow(new ProductNotFoundException("Product not found with ID: " + id));

        MvcTestResult testResult = mockMvcTester
                .get()
                .uri("/api/products/1")
                .exchange();

        assertThat(testResult)
                .hasStatus(HttpStatus.NOT_FOUND)
                .bodyJson()
                .extractingPath("$.title")
                .isEqualTo("Product Not Found");

        assertThat(testResult)
                .bodyJson()
                .extractingPath("$.detail")
                .isEqualTo("Product not found with ID: 1");

        assertThat(testResult)
                .bodyJson()
                .extractingPath("$.timestamp")
                .isNotNull();

    }


}