package com.flexbox.backend.admin.catalog.service;

import com.flexbox.backend.admin.catalog.dto.request.AdminCreateBoxProductRequest;
import com.flexbox.backend.admin.catalog.dto.request.AdminCreateBoxPriceRequest;
import com.flexbox.backend.admin.catalog.dto.request.AdminCreateBoxRequest;
import com.flexbox.backend.admin.catalog.dto.response.AdminBoxPriceResponse;
import com.flexbox.backend.admin.catalog.dto.response.AdminBoxProductResponse;
import com.flexbox.backend.admin.catalog.dto.response.AdminBoxResponse;
import com.flexbox.backend.catalog.box.model.SubscriptionBox;
import com.flexbox.backend.catalog.box.model.SubscriptionBoxPrice;
import com.flexbox.backend.catalog.box.model.SubscriptionBoxProduct;
import com.flexbox.backend.catalog.box.model.SubscriptionBoxProductId;
import com.flexbox.backend.catalog.box.repository.SubscriptionBoxPriceRepository;
import com.flexbox.backend.catalog.box.repository.SubscriptionBoxProductRepository;
import com.flexbox.backend.catalog.box.repository.SubscriptionBoxRepository;
import com.flexbox.backend.catalog.product.model.Product;
import com.flexbox.backend.catalog.product.repository.ProductRepository;
import com.flexbox.backend.common.exception.BusinessRuleException;
import com.flexbox.backend.common.exception.ResourceAlreadyExistsException;
import com.flexbox.backend.common.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminBoxServiceTest {

    @Mock
    SubscriptionBoxRepository boxRepository;

    @Mock
    SubscriptionBoxProductRepository boxProductRepository;

    @Mock
    ProductRepository productRepository;

    @Mock
    SubscriptionBoxPriceRepository priceRepository;

    @InjectMocks
    AdminBoxService boxService;

    @Test
    void createBox_shouldCreateBox_whenNameDoesNotExist() {

        var request = new AdminCreateBoxRequest(
                "Yoga Box",
                "Monthly yoga fitness box",
                "/images/yoga_box.jpg",
                10,
                true
        );

        given(boxRepository.existsByNameIgnoreCase(request.name()))
                .willReturn(false);

        var savedBox = new SubscriptionBox();
        savedBox.setId(1L);
        savedBox.setName(request.name());
        savedBox.setDescription(request.description());
        savedBox.setImageFile(request.imagePath());
        savedBox.setAvailableUnits(request.availableUnits());
        savedBox.setIsActive(request.isActive());

        given(boxRepository.save(any(SubscriptionBox.class)))
                .willReturn(savedBox);

        var result = boxService.createBox(request);

        var expected = new AdminBoxResponse(
                1L,
                "Yoga Box",
                "Monthly yoga fitness box",
                "/images/yoga_box.jpg",
                10,
                true
        );


        assertThat(result).isEqualTo(expected);

        ArgumentCaptor<SubscriptionBox> boxCaptor =
                ArgumentCaptor.forClass(SubscriptionBox.class);

        then(boxRepository)
                .should()
                .save(boxCaptor.capture());

        then(boxRepository)
                .should()
                .existsByNameIgnoreCase(request.name());

        var capturedBox = boxCaptor.getValue();

        assertThat(capturedBox.getName())
                .isEqualTo(request.name());

        assertThat(capturedBox.getDescription())
                .isEqualTo(request.description());

        assertThat(capturedBox.getImageFile())
                .isEqualTo(request.imagePath());

        assertThat(capturedBox.getAvailableUnits())
                .isEqualTo(request.availableUnits());

        assertThat(capturedBox.getIsActive())
                .isEqualTo(request.isActive());

    }

    @Test
    void createBox_shouldThrowException_whenNameAlreadyExists() {

        var request = new AdminCreateBoxRequest(
                "Yoga Box",
                "Monthly yoga fitness box",
                "/images/yoga_box.jpg",
                10,
                true
        );

        given(boxRepository.existsByNameIgnoreCase(request.name()))
                .willReturn(true);

        assertThatThrownBy(() -> boxService.createBox(request))
                .isInstanceOf(ResourceAlreadyExistsException.class)
                .hasMessageContaining(request.name());

        then(boxRepository)
                .should()
                .existsByNameIgnoreCase(request.name());

        then(boxRepository)
                .should(never())
                .save(any(SubscriptionBox.class));
    }

    @Test
    void deactivateBox_shouldDeactivateBox_whenBoxExists() {

        Long boxId = 1L;

        given(boxRepository.updateIsActiveById(false, boxId))
                .willReturn(1);

        boxService.deactivateBox(boxId);

        then(boxRepository)
                .should()
                .updateIsActiveById(false, boxId);
    }

    @Test
    void deactivateBox_shouldThrowException_whenBoxDoesNotExist() {

        Long boxId = 999L;

        given(boxRepository.updateIsActiveById(false, boxId))
                .willReturn(0);

        assertThatThrownBy(() -> boxService.deactivateBox(boxId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.valueOf(boxId));

        then(boxRepository)
                .should()
                .updateIsActiveById(false, boxId);
    }


    @Test
    void createBoxProduct_shouldCreateProduct_whenValid() {

        var box = new SubscriptionBox();
        box.setId(1L);
        box.setName("Yoga Box");
        box.setDescription("Monthly yoga fitness box");
        box.setImageFile("/images/yoga_box.jpg");
        box.setAvailableUnits(10);
        box.setIsActive(true);

        var product = new Product();
        product.setId(1L);
        product.setBrand("Sunny");
        product.setName("Sunscreen");
        product.setDescription("SPF 60 sunscreen");
        product.setCostPerUnit(new BigDecimal("1.02"));
        product.setIsActive(true);

       Long boxId = box.getId();
       Long productId = product.getId();

        var request = new AdminCreateBoxProductRequest(
                productId,
                2
        );

        var boxProductId = new SubscriptionBoxProductId(
                boxId,
                productId
        );

        given(boxRepository.findById(boxId))
                .willReturn(Optional.of(box));

        given(productRepository.findById(request.productId()))
                .willReturn(Optional.of(product));


        given(boxProductRepository.existsById(boxProductId))
                .willReturn(false);

        var savedBoxProduct = new SubscriptionBoxProduct();
        savedBoxProduct.setId(boxProductId);
        savedBoxProduct.setSubscriptionBox(box);
        savedBoxProduct.setProduct(product);
        savedBoxProduct.setQuantity(request.quantity());

        given(boxProductRepository.save(any(SubscriptionBoxProduct.class)))
                .willReturn(savedBoxProduct);

        var result = boxService.createBoxProduct(boxId, request);

        var expected = new AdminBoxProductResponse(
                1L,
                1L,
                "Sunscreen",
                2
                );

        assertThat(result).isEqualTo(expected);

        ArgumentCaptor<SubscriptionBoxProduct> boxProductCaptor =
                ArgumentCaptor.forClass(SubscriptionBoxProduct.class);

        then(boxProductRepository)
                .should()
                .save(boxProductCaptor.capture());

        then(boxProductRepository)
                .should()
                .existsById(boxProductId);

        var capturedBoxProduct = boxProductCaptor.getValue();

        assertThat(capturedBoxProduct.getSubscriptionBox())
                .isEqualTo(box);

        assertThat(capturedBoxProduct.getProduct())
                .isEqualTo(product);

        assertThat(capturedBoxProduct.getQuantity())
                .isEqualTo(request.quantity());


        then(boxRepository)
                .should()
                .findById(boxId);

        then(productRepository)
                .should()
                .findById(productId);

        then(boxProductRepository)
                .should()
                .existsById(boxProductId);
    }

    @Test
    void createBoxProduct_shouldThrowException_whenBoxDoesNotExist() {

        Long boxId = 1L;

        var request = new AdminCreateBoxProductRequest(
                10L,
                2
        );

        given(boxRepository.findById(boxId))
                .willReturn(Optional.empty());

        assertThatThrownBy(() ->
                boxService.createBoxProduct(boxId, request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.valueOf(boxId));

        then(boxRepository)
                .should()
                .findById(boxId);

        then(productRepository)
                .shouldHaveNoInteractions();

        then(boxProductRepository)
                .shouldHaveNoInteractions();
    }

    @Test
    void createBoxProduct_shouldThrowException_whenProductDoesNotExist() {

        var box = new SubscriptionBox();
        box.setId(1L);
        box.setName("Yoga Box");
        box.setDescription("Monthly yoga fitness box");
        box.setImageFile("/images/yoga_box.jpg");
        box.setAvailableUnits(10);
        box.setIsActive(true);

        Long boxId = box.getId();
        Long productId = 10L;

        var request = new AdminCreateBoxProductRequest(
                productId,
                2
        );

        given(boxRepository.findById(boxId))
                .willReturn(Optional.of(box));

        given(productRepository.findById(productId))
                .willReturn(Optional.empty());

        assertThatThrownBy(() ->
                boxService.createBoxProduct(boxId, request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.valueOf(productId));

        then(boxRepository)
                .should()
                .findById(boxId);

        then(productRepository)
                .should()
                .findById(productId);

        then(boxProductRepository)
                .shouldHaveNoInteractions();
    }

    @Test
    void createBoxProduct_shouldThrowException_whenProductIsInactive() {

        var box = new SubscriptionBox();
        box.setId(1L);
        box.setName("Yoga Box");
        box.setDescription("Monthly yoga fitness box");
        box.setImageFile("/images/yoga_box.jpg");
        box.setAvailableUnits(10);
        box.setIsActive(true);

        var product = new Product();
        product.setId(1L);
        product.setBrand("Sunny");
        product.setName("Sunscreen");
        product.setDescription("SPF 60 sunscreen");
        product.setCostPerUnit(new BigDecimal("1.02"));
        product.setIsActive(false);

        Long boxId = box.getId();
        Long productId = product.getId();

        var request = new AdminCreateBoxProductRequest(
                productId,
                2
        );

        given(boxRepository.findById(boxId))
                .willReturn(Optional.of(box));

        given(productRepository.findById(productId))
                .willReturn(Optional.of(product));

        assertThatThrownBy(() ->
                boxService.createBoxProduct(boxId, request))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("inactive");

        then(boxRepository)
                .should()
                        .findById(boxId);

        then(productRepository)
                .should()
                        .findById(productId);

        then(boxProductRepository)
                .shouldHaveNoInteractions();
    }

    @Test
    void createBoxProduct_shouldThrowException_whenProductAlreadyExists() {

        var box = new SubscriptionBox();
        box.setId(1L);
        box.setName("Yoga Box");
        box.setDescription("Monthly yoga fitness box");
        box.setImageFile("/images/yoga_box.jpg");
        box.setAvailableUnits(10);
        box.setIsActive(true);

        var product = new Product();
        product.setId(1L);
        product.setBrand("Sunny");
        product.setName("Sunscreen");
        product.setDescription("SPF 60 sunscreen");
        product.setCostPerUnit(new BigDecimal("1.02"));
        product.setIsActive(true);

        Long boxId = box.getId();
        Long productId = product.getId();

        var request = new AdminCreateBoxProductRequest(
                productId,
                2
        );

        var boxProductId = new SubscriptionBoxProductId(
                boxId,
                productId
        );

        given(boxRepository.findById(boxId))
                .willReturn(Optional.of(box));

        given(productRepository.findById(productId))
                .willReturn(Optional.of(product));

        given(boxProductRepository.existsById(boxProductId))
                .willReturn(true);

        assertThatThrownBy(() ->
                boxService.createBoxProduct(boxId, request))
                .isInstanceOf(ResourceAlreadyExistsException.class)
                .hasMessageContaining(String.valueOf(productId));

        then(boxRepository)
                .should()
                .findById(boxId);

        then(productRepository)
                .should()
                .findById(productId);

        then(boxProductRepository)
                .should(never())
                .save(any());
    }

    @Test
    void setBoxPrice_shouldCreatePrice_whenBoxExists() {

        var box = new SubscriptionBox();
        box.setId(1L);
        box.setName("Yoga Box");
        box.setDescription("Monthly yoga fitness box");
        box.setImageFile("/images/yoga_box.jpg");
        box.setAvailableUnits(10);
        box.setIsActive(true);

        Long boxId = box.getId();

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

        var request = new AdminCreateBoxPriceRequest(
                new BigDecimal("49.99"),
                startsAt,
                endsAt
        );

        given(boxRepository.findById(boxId))
                .willReturn(Optional.of(box));

        var savedPrice = new SubscriptionBoxPrice();
        savedPrice.setId(1L);
        savedPrice.setSubscriptionBox(box);
        savedPrice.setAmount(request.amount());
        savedPrice.setStartsAt(request.startsAt());
        savedPrice.setEndsAt(request.endsAt());

        given(priceRepository.save(any(SubscriptionBoxPrice.class)))
                .willReturn(savedPrice);

        var result = boxService.setBoxPrice(boxId, request);

        var expected = new AdminBoxPriceResponse(
                1L,
                1L,
                new BigDecimal("49.99"),
                startsAt,
                endsAt
        );

        assertThat(result)
                .isEqualTo(expected);

        ArgumentCaptor<SubscriptionBoxPrice> priceCaptor =
                ArgumentCaptor.forClass(SubscriptionBoxPrice.class);

        then(priceRepository)
                .should()
                        .save(priceCaptor.capture());

        var capturedPrice = priceCaptor.getValue();


        assertThat(capturedPrice.getSubscriptionBox())
                .isEqualTo(box);

        assertThat(capturedPrice.getAmount())
                .isEqualByComparingTo(request.amount());

        assertThat(capturedPrice.getStartsAt())
                .isEqualTo(request.startsAt());

        assertThat(capturedPrice.getEndsAt())
                .isEqualTo(request.endsAt());

        then(boxRepository)
                .should()
                .findById(boxId);

        then(priceRepository)
                .should()
                .save(any(SubscriptionBoxPrice.class));
    }

    @Test
    void setBoxPrice_shouldThrowException_whenBoxDoesNotExist() {

        Long boxId = 999L;

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

        var request = new AdminCreateBoxPriceRequest(
                new BigDecimal("49.99"),
                startsAt,
                endsAt
        );

        given(boxRepository.findById(boxId))
                .willReturn(Optional.empty());

        assertThatThrownBy(() ->
                boxService.setBoxPrice(boxId, request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.valueOf(boxId));

        then(priceRepository)
                .shouldHaveNoInteractions();

    }
}