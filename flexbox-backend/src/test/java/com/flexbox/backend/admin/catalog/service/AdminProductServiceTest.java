package com.flexbox.backend.admin.catalog.service;

import com.flexbox.backend.admin.catalog.dto.response.ProductDetailResponse;
import com.flexbox.backend.admin.catalog.dto.response.ProductSummaryResponse;
import com.flexbox.backend.catalog.product.model.Category;
import com.flexbox.backend.catalog.product.model.Product;
import com.flexbox.backend.admin.catalog.service.AdminProductService;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminProductServiceTest {

    @Mock
    ProductRepository productRepository;

    @Mock
    ProductInventoryRepository inventoryRepository;

    @InjectMocks
    AdminProductService adminProductService;

    private Product sunscreen;
    private Product socks;
    private ProductInventory sunscreenInventory;

    private static final OffsetDateTime CURRENT_TIME = OffsetDateTime.of(
            2026, 8, 2, 23, 38, 39, 657_650_000, ZoneOffset.UTC
    );


    @BeforeEach
    void setup() {

        var personalCare = new Category();
        personalCare.setId(1L);
        personalCare.setName("personal care");

        sunscreen = new Product();
        sunscreen.setId(2L);
        sunscreen.setCategory(personalCare);
        sunscreen.setSku("SKU-102");
        sunscreen.setBrand("Sunny");
        sunscreen.setName("Sunscreen");
        sunscreen.setDescription("SPF 60 sunscreen");
        sunscreen.setCostPerUnit(BigDecimal.valueOf(0.95));
        sunscreen.setIsActive(true);
        sunscreen.setCreatedAt(CURRENT_TIME.minusDays(1)); // Aug 1st 2026
        sunscreen.setUpdatedAt(CURRENT_TIME.minusDays(1)); // Aug 1st 2026

        socks = new Product();
        socks.setId(1L);
        socks.setCategory(personalCare);
        socks.setSku("SKU-101");
        socks.setBrand("Cool Socks");
        socks.setName("Socks");
        socks.setDescription("Crew socks");
        socks.setCostPerUnit(BigDecimal.valueOf(1.24));
        socks.setIsActive(true);
        socks.setCreatedAt(CURRENT_TIME.plusDays(1)); // Aug 2nd 2026
        socks.setUpdatedAt(CURRENT_TIME.plusDays(1)); // Aug 2nd 2026

        sunscreenInventory = new ProductInventory();
        sunscreenInventory.setId(1L);
        sunscreenInventory.setProduct(sunscreen);
        sunscreenInventory.setInStock(10);
        sunscreenInventory.setReserved(2);
        sunscreenInventory.setCreatedAt(CURRENT_TIME.plusDays(1));
        sunscreenInventory.setUpdatedAt(CURRENT_TIME.plusDays(2));

    }

    @Test
    void getProducts_success_shouldReturnMappedCollection() {

        given(productRepository.findAll())
                .willReturn(List.of(sunscreen, socks));

        var result = adminProductService.getProducts();

        assertThat(result.items()).isNotEmpty().hasSize(2);
        assertThat(result.items())
                .extracting(ProductSummaryResponse::id)
                        .containsExactly(sunscreen.getId(), socks.getId());

        verify(productRepository).findAll();
    }

    @Test
    void getProductById_success_shouldReturnDetails() {

        Long productId = 2L;

        given(productRepository.findById(productId))
                .willReturn(Optional.of(sunscreen));

        given(inventoryRepository.findByProduct_Id(productId))
                .willReturn(Optional.of(sunscreenInventory));

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

        given(productRepository.findById(productId))
                .willReturn(Optional.of(socks));

        given(inventoryRepository.findByProduct_Id(productId))
                .willReturn(Optional.empty());

        var result = adminProductService.getProductById(productId);

        assertThat(result.inventory().inStock()).isZero();
        assertThat(result.inventory().reserved()).isZero();

        verify(productRepository).findById(productId);
        verify(inventoryRepository).findByProduct_Id(productId);
    }

    @Test
    void getProductById_shouldThrowException_whenProductNotFound() {
        Long productId = 3L;

        given(productRepository.findById(productId))
                .willReturn(Optional.empty());

        assertThatThrownBy(() -> adminProductService
                .getProductById(productId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(productId.toString());

        verifyNoInteractions(inventoryRepository);
    }
}