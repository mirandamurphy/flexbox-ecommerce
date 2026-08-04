package com.flexbox.backend.catalog.controller;

import com.flexbox.backend.catalog.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;
import org.springframework.core.io.ClassPathResource;

import static org.assertj.core.api.Assertions.assertThat;


@WebMvcTest(controllers = ProductController.class)
class ProductControllerTest {

    @Autowired
    MockMvcTester mockMvcTester;

    @MockitoBean
    ProductService productService;



    @Test
    void getAllProducts_shouldReturn200_AndProductListResponse_whenProductsExist() {

        var expected = new ClassPathResource("responses/products/get-all-products-response.json", ProductControllerTest.class);

        MvcTestResult testResult = mockMvcTester
                .get()
                .uri("/products")
                .exchange();

        assertThat(testResult)
                .hasStatus(HttpStatus.OK)
                .bodyJson()
                .isLenientlyEqualTo(expected);
    }

    @Test
    void getProductById_shouldReturn200_AndProductDetail_whenProductExists() {

        var expected = new ClassPathResource("responses/products/get-product-by-id-response.json", ProductControllerTest.class);

        MvcTestResult testResult = mockMvcTester
                .get()
                .uri("/products/4")
                .exchange();

        assertThat(testResult)
                .hasStatus(HttpStatus.OK)
                .bodyJson()
                .isLenientlyEqualTo(expected);
    }

    @Test
    void getProductById_shouldReturn404_whenProductNotFound() {


    }


}