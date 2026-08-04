package com.flexbox.backend.catalog.service;

import com.flexbox.backend.catalog.dto.product.ProductDetail;
import com.flexbox.backend.catalog.exception.ProductNotFoundException;
import com.flexbox.backend.catalog.model.Category;
import com.flexbox.backend.catalog.model.Product;
import com.flexbox.backend.catalog.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductService productService;

    private Product product1;
    private Product product2;

    private Category category;

    private static final OffsetDateTime CURRENT_TIME = OffsetDateTime.of(
            2026, 8, 2, 23, 38, 39, 657_650_000, ZoneOffset.UTC
    );


    @BeforeEach
    void setup() {

        // Product Category
        category = new Category();
        category.setId(1L);
        category.setName("Protein");

        // Product 1
        product1 = new Product();
        product1.setId(1L);
        product1.setCategory(category);
        product1.setSku("PM-PB-001");
        product1.setBrand("Protein Mix");
        product1.setName("PB Protein Powder");
        product1.setDescription("Peanut butter protein powder pack with 34g of protein");
        product1.setCostPerUnit(BigDecimal.valueOf(0.95));
        product1.setIsActive(true);
        product1.setCreatedAt(CURRENT_TIME.minusDays(1)); // August 1 2026
        product1.setUpdatedAt(CURRENT_TIME.minusDays(1)); // August 1 2026

        // Product 2
        product2 = new Product();
        product2.setId(2L);
        product2.setCategory(category);
        product2.setSku("PM-SB-001");
        product2.setBrand("Protein Mix");
        product2.setName("Strawberry Protein Powder");
        product2.setDescription("Strawberry protein powder pack with 36g of protein");
        product2.setCostPerUnit(BigDecimal.valueOf(0.99));
        product2.setIsActive(true);
        product2.setCreatedAt(CURRENT_TIME); // August 2 2026
        product2.setUpdatedAt(CURRENT_TIME); // August 2 2026

    }

    @Test
    void getAllProducts_shouldReturnCollectionDTO() {

        Long id1 = 1L;
        Long id2 = 2L;

        when(productRepository.findAll())
                .thenReturn(List.of(product1, product2));

        var result = productService.getAllProducts();

        assertThat(result.products())
                .isNotEmpty()
                .hasSize(2);

        verify(productRepository).findAll();

    }

    @Test
    void getProductById_shouldReturnDetailsDTO() {
        Long id = 1L;

        when(productRepository.findById(id))
                .thenReturn(Optional.of(product1));

        var result = productService.getProductById(id);

        assertThat(result).isNotNull()
                .isExactlyInstanceOf(ProductDetail.class);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.brand()).isEqualTo("Protein Mix");
        assertThat(result.name()).isEqualTo("PB Protein Powder");
        assertThat(result.description()).isEqualTo("Peanut butter protein powder pack with 34g of protein");
        assertThat(result.category().id()).isEqualTo(1L);
        assertThat(result.category().name()).isEqualTo("Protein");
        assertThat(result.isActive()).isTrue();


        // Verify ProductService called Mock Dependency in expected way
        verify(productRepository).findById(id);
    }

    @Test
    void getProductById_shouldThrow_ProductNotFoundException() {
        Long id = 3L;


        when(productRepository.findById(id))
                .thenReturn(Optional.empty());


        assertThatThrownBy(() -> productService
                .getProductById(id))
                .isInstanceOf(ProductNotFoundException.class)
                        .hasMessageContaining(
                                "Product not found with ID: 3"
                        );


        verify(productRepository).findById(id);
    }
}