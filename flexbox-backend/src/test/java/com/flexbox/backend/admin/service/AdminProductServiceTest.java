package com.flexbox.backend.admin.service;

import com.flexbox.backend.admin.box.dto.response.ProductDetailResponse;
import com.flexbox.backend.admin.box.dto.response.ProductSummaryResponse;
import com.flexbox.backend.catalog.product.model.Category;
import com.flexbox.backend.catalog.product.model.Product;
import com.flexbox.backend.admin.box.service.AdminProductService;
import com.flexbox.backend.catalog.product.model.ProductInventory;
import com.flexbox.backend.catalog.product.repository.ProductInventoryRepository;
import com.flexbox.backend.catalog.product.repository.ProductRepository;
import com.flexbox.backend.common.exception.ResourceNotFoundException;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminProductServiceTest {

    @Mock
    ProductRepository productRepository;

    @Mock
    ProductInventoryRepository inventoryRepository;

    @InjectMocks
    AdminProductService adminProductService;

    private Product product2;
    private Product product1;
    private ProductInventory inventory;

    private static final OffsetDateTime CURRENT_TIME = OffsetDateTime.of(
            2026, 8, 2, 23, 38, 39, 657_650_000, ZoneOffset.UTC
    );


    @BeforeEach
    void setup() {

        var category = new Category();
        category.setId(1L);
        category.setName("personal care");

        product2 = new Product();
        product2.setId(2L);
        product2.setCategory(category);
        product2.setSku("SKU-102");
        product2.setBrand("Sunny");
        product2.setName("Sunscreen");
        product2.setDescription("SPF 60 sunscreen");
        product2.setCostPerUnit(BigDecimal.valueOf(0.95));
        product2.setIsActive(true);
        product2.setCreatedAt(CURRENT_TIME.minusDays(1)); // Aug 1st 2026
        product2.setUpdatedAt(CURRENT_TIME.minusDays(1)); // Aug 1st 2026

        product1 = new Product();
        product1.setId(1L);
        product1.setCategory(category);
        product1.setSku("SKU-101");
        product1.setBrand("Cool Socks");
        product1.setName("Socks");
        product1.setDescription("Crew socks");
        product1.setCostPerUnit(BigDecimal.valueOf(1.24));
        product1.setIsActive(true);
        product2.setCreatedAt(CURRENT_TIME.plusDays(1)); // Aug 2nd 2026
        product2.setUpdatedAt(CURRENT_TIME.plusDays(1)); // Aug 2nd 2026

        inventory = new ProductInventory();
        inventory.setId(1L);
        inventory.setProduct(product2);
        inventory.setInStock(10);
        inventory.setReserved(2);
        inventory.setCreatedAt(CURRENT_TIME.plusDays(1));
        inventory.setUpdatedAt(CURRENT_TIME.plusDays(2));

    }

    @Test
    void getProducts_success_shouldReturnMappedCollection() {


        when(productRepository.findAll())
                .thenReturn(List.of(product2, product1));

        var result = adminProductService.getProducts();

        assertThat(result.items())
                .isNotEmpty()
                .hasSize(2);
        assertThat(result.items())
                .extracting(ProductSummaryResponse::id)
                        .containsExactly(product2.getId(), product1.getId());

        verify(productRepository).findAll();
    }

    @Test
    void getProductById_success_shouldReturnDetails() {

        Long productId = 2L;

        when(productRepository.findById(productId))
                .thenReturn(Optional.of(product2));

        when(inventoryRepository.findByProduct_Id(productId))
                .thenReturn(Optional.of(inventory));

        var result = adminProductService.getProductById(productId);

        assertThat(result).isNotNull()
                .isExactlyInstanceOf(ProductDetailResponse.class);

        assertThat(result.id()).isEqualTo(2L);
        assertThat(result.brand()).isEqualTo("Sunny");
        assertThat(result.name()).isEqualTo("Sunscreen");
        assertThat(result.description()).isEqualTo("SPF 60 sunscreen");
        assertThat(result.category().id()).isEqualTo(1L);
        assertThat(result.category().name()).isEqualTo("personal care");
        assertThat(result.isActive()).isTrue();
        assertThat(result.inventory().inStock()).isEqualTo(10);
        assertThat(result.inventory().reserved()).isEqualTo(2);

        verify(productRepository).findById(productId);
        verify(inventoryRepository).findByProduct_Id(productId);
    }

    @Test
    void getProductById_inventoryDefaultsToZero_whenNoInventory() {
        Long productId = 1L;
        when(productRepository.findById(productId))
                .thenReturn(Optional.of(product1));

        when(inventoryRepository.findByProduct_Id(productId))
                .thenReturn(Optional.empty());

        var result = adminProductService.getProductById(productId);

        assertThat(result.inventory().inStock()).isZero();
        assertThat(result.inventory().reserved()).isZero();
    }

    @Test
    void getProductById_shouldThrowException_whenProductNotFound() {
        Long productId = 3L;

        when(productRepository.findById(productId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> adminProductService
                .getProductById(productId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(productId.toString());

        verifyNoInteractions(inventoryRepository);
    }
}