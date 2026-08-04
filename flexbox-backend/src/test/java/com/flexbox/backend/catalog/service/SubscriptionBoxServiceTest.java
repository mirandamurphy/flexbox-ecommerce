package com.flexbox.backend.catalog.service;

import com.flexbox.backend.catalog.dto.subscriptionbox.SubscriptionBoxDetail;
import com.flexbox.backend.catalog.exception.SubscriptionBoxPriceNotFoundException;
import com.flexbox.backend.catalog.model.SubscriptionBox;
import com.flexbox.backend.catalog.model.SubscriptionBoxPrice;
import com.flexbox.backend.catalog.model.SubscriptionBoxProduct;
import com.flexbox.backend.catalog.repository.SubscriptionBoxPriceRepository;
import com.flexbox.backend.catalog.repository.SubscriptionBoxProductRepository;
import com.flexbox.backend.catalog.repository.SubscriptionBoxRepository;
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
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class SubscriptionBoxServiceTest {

    @Mock
    private SubscriptionBoxRepository subscriptionBoxRepository;

    @Mock
    private SubscriptionBoxPriceRepository subscriptionBoxPriceRepository;

    @Mock
    private SubscriptionBoxProductRepository subscriptionBoxProductRepository;

    @InjectMocks
    private SubscriptionBoxService subscriptionBoxService;

    private SubscriptionBox box1;
    private SubscriptionBox box2;

    private SubscriptionBoxPrice price1;
    private SubscriptionBoxPrice price2;

    private static final OffsetDateTime CURRENT_TIME = OffsetDateTime.of(
            2026, 8, 2, 23, 38, 39, 657_650_000, ZoneOffset.UTC
    );

    @BeforeEach
    void setup() {

        // Subscription Box 1
        box1 = new SubscriptionBox();
        box1.setId(1L);
        box1.setName("Monthly Box");
        box1.setAvailableUnits(10);
        box1.setIsActive(true);

        // Subscription Box 2
        box2 = new SubscriptionBox();
        box2.setId(2L);
        box2.setName("Quarterly Box");
        box2.setAvailableUnits(20);
        box2.setIsActive(true);

        // Subscription Box Price 1
        price1 = new SubscriptionBoxPrice();
        price1.setId(1L);
        price1.setAmount(new BigDecimal("29.99"));
        price1.setCurrency("CAD");
        price1.setStartsAt(CURRENT_TIME.minusDays(1)); // Aug 1st 2026
        price1.setEndsAt(CURRENT_TIME.plusDays(5)); // Aug 7th 2026
        price1.setStripePriceId("stripe_price_123");
        price1.setSubscriptionBox(box1);

        // Subscription Box Price 2
        price2 = new SubscriptionBoxPrice();
        price2.setId(2L);
        price2.setAmount(new BigDecimal("49.99"));
        price2.setCurrency("CAD");
        price2.setStartsAt(CURRENT_TIME.minusDays(1)); // Aug 1st 2026
        price2.setEndsAt(CURRENT_TIME.plusDays(5)); // Aug 7th 2026
        price2.setStripePriceId("stripe_price_124");
        price2.setSubscriptionBox(box2);
    }

    @Test
    void getAllSubscriptionBoxes_shouldReturnCollectionDTO() {

        Long id1 = 1L;
        Long id2 = 2L;

        when(subscriptionBoxRepository.findAll())
                .thenReturn(List.of(box1, box2));

        when(subscriptionBoxPriceRepository.findActivePriceBySubscriptionBoxId(eq(id1), any()))
                .thenReturn(Optional.of(price1));

        when(subscriptionBoxPriceRepository.findActivePriceBySubscriptionBoxId(eq(id2), any()))
                .thenReturn(Optional.of(price2));

        var result = subscriptionBoxService.getAllSubscriptionBoxes();

        assertThat(result.subscriptionBoxes())
                .isNotEmpty()
                .hasSize(2);

        verify(subscriptionBoxRepository).findAll();
        verify(subscriptionBoxPriceRepository).findActivePriceBySubscriptionBoxId(id1, any());
        verify(subscriptionBoxPriceRepository).findActivePriceBySubscriptionBoxId(id2, any());


    }

    @Test
    void getAllSubscriptionBoxes_shouldThrow_SubscriptionBoxPriceNotFoundException() {

        when(subscriptionBoxRepository.findAll())
                .thenReturn(List.of(box1));

        when(subscriptionBoxPriceRepository.findActivePriceBySubscriptionBoxId(
                eq(1L),
                any()
        )).thenReturn(Optional.empty());

        assertThatThrownBy(() -> subscriptionBoxService.getAllSubscriptionBoxes())
                .isInstanceOf(SubscriptionBoxPriceNotFoundException.class)
                .hasMessageContaining(
                        "Active price not found for box ID: 1"
                );

        verify(subscriptionBoxRepository)
                .findAll();

    }

    @Test
    void getSubscriptionBoxById_shouldReturnDetailsDTO() {

        Long id = 1L;
        List<SubscriptionBoxProduct> products = List.of();

        when(subscriptionBoxRepository.findById(id))
                .thenReturn(Optional.of(box1));

        when(subscriptionBoxPriceRepository.findActivePriceBySubscriptionBoxId(
                eq(id), any()))
                .thenReturn(Optional.of(price1));

        when(subscriptionBoxProductRepository.findAllBySubscriptionBoxId(id))
                .thenReturn(products);

        var result = subscriptionBoxService.getSubscriptionBoxById(id);

        assertThat(result).isNotNull()
                .isExactlyInstanceOf(SubscriptionBoxDetail.class);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("Monthly Box");
        assertThat(result.price().amount()).isEqualByComparingTo("29.99");
        assertThat(result.price().currency()).isEqualTo("CAD");


        verify(subscriptionBoxRepository).findById(id);
        verify(subscriptionBoxPriceRepository).findActivePriceBySubscriptionBoxId(eq(id), any(OffsetDateTime.class));
        verify(subscriptionBoxProductRepository).findAllBySubscriptionBoxId(id);

    }
}