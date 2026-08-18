package com.flexbox.backend.catalog.box.service;

import com.flexbox.backend.catalog.box.dto.BoxResponse;

import com.flexbox.backend.catalog.box.model.SubscriptionBox;
import com.flexbox.backend.catalog.box.model.SubscriptionBoxPrice;
import com.flexbox.backend.catalog.box.service.SubscriptionBoxService;
import com.flexbox.backend.catalog.box.repository.SubscriptionBoxPriceRepository;
import com.flexbox.backend.catalog.box.repository.SubscriptionBoxRepository;
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
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class SubscriptionBoxServiceTest {

    @Mock
    private SubscriptionBoxRepository boxRepository;

    @Mock
    private SubscriptionBoxPriceRepository boxPriceRepository;

    @InjectMocks
    private SubscriptionBoxService boxService;

    private SubscriptionBox runningBox;
    private SubscriptionBox yogaBox;

    private SubscriptionBoxPrice runningBoxPrice;
    private SubscriptionBoxPrice yogaBoxPrice;

    private static final OffsetDateTime CURRENT_TIME = OffsetDateTime.of(
            2026, 8, 2, 23, 38, 39, 657_650_000, ZoneOffset.UTC
    );

    @BeforeEach
    void setup() {

        runningBox = new SubscriptionBox();
        runningBox.setId(1L);
        runningBox.setName("Running Box");
        runningBox.setDescription("A running box");
        runningBox.setImageFile("/images/running_box.jpg");
        runningBox.setIsActive(true);

        yogaBox = new SubscriptionBox();
        yogaBox.setId(2L);
        yogaBox.setName("Yoga Box");
        yogaBox.setDescription("A yoga box");
        yogaBox.setImageFile("/images/yoga_box.jpg");
        yogaBox.setIsActive(true);

        runningBoxPrice = new SubscriptionBoxPrice();
        runningBoxPrice.setId(1L);
        runningBoxPrice.setAmount(new BigDecimal("29.99"));
        runningBoxPrice.setCurrency("CAD");
        runningBoxPrice.setStartsAt(CURRENT_TIME.minusDays(1)); // Aug 1st 2026
        runningBoxPrice.setEndsAt(CURRENT_TIME.plusDays(5)); // Aug 7th 2026
        runningBoxPrice.setStripePriceId("stripe_price_123");
        runningBoxPrice.setSubscriptionBox(runningBox);

        yogaBoxPrice = new SubscriptionBoxPrice();
        yogaBoxPrice.setId(2L);
        yogaBoxPrice.setAmount(new BigDecimal("39.99"));
        yogaBoxPrice.setCurrency("CAD");
        yogaBoxPrice.setStartsAt(CURRENT_TIME.minusDays(1)); // Aug 1st 2026
        yogaBoxPrice.setEndsAt(CURRENT_TIME.plusDays(5)); // Aug 7th 2026
        yogaBoxPrice.setStripePriceId("stripe_price_124");
        yogaBoxPrice.setSubscriptionBox(yogaBox);
    }

    @Test
    void getBoxes_shouldReturnBoxesWithPrices_whenBoxesHaveActivePrice() {

        given(boxRepository.findAllByIsActiveTrueOrderByIdAsc())
                .willReturn(List.of(runningBox, yogaBox));

        given(boxPriceRepository.findCurrentPrices(anyList(), any(OffsetDateTime.class)))
                .willReturn(List.of(runningBoxPrice, yogaBoxPrice));


        var result = boxService.getBoxes();

        assertThat(result.items())
                .isNotEmpty()
                .hasSize(2);

        verify(boxRepository).findAllByIsActiveTrueOrderByIdAsc();
        verify(boxPriceRepository).findCurrentPrices(anyList(), any(OffsetDateTime.class));
    }

    /*
    Customers should receive a 'blank' screen if there is no active boxes
    E.g., If boxes are not launched yet or if boxes are all sold out.
     */
    @Test
    void getBoxes_noActiveBoxes_shouldReturnEmptyCollection() {

        given(boxRepository.findAllByIsActiveTrueOrderByIdAsc())
                .willReturn(List.of());

        given(boxPriceRepository.findCurrentPrices(anyList(), any(OffsetDateTime.class)))
                .willReturn(List.of());

        var result = boxService.getBoxes();

        assertThat(result.items()).isEmpty();

        verify(boxRepository).findAllByIsActiveTrueOrderByIdAsc();
        verify(boxPriceRepository).findCurrentPrices(anyList(), any(OffsetDateTime.class));
    }

    /*
    If a box does not have an active price a customer should not
    be able to see it.
     */
    @Test
    void getBoxes_shouldThrowException_whenActiveBoxPriceNotFoundException() {


        given(boxRepository.findAllByIsActiveTrueOrderByIdAsc())
                .willReturn(List.of(runningBox));

        given(boxPriceRepository.findCurrentPrices(
                anyList(),
                any(OffsetDateTime.class)))
                .willReturn(List.of());

        assertThatThrownBy(() -> boxService.getBoxes())
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("1");

        verify(boxRepository)
                .findAllByIsActiveTrueOrderByIdAsc();

    }

    @Test
    void getBoxById_shouldReturnBoxWithPrice_whenBoxHasActivePrice() {

        Long boxId = 1L;

        given(boxRepository.findById(boxId))
                .willReturn(Optional.of(runningBox));

        given(boxPriceRepository.findCurrentPrice(
                eq(boxId), any()))
                .willReturn(Optional.of(runningBoxPrice));


        var result = boxService.getBoxById(boxId);

        assertThat(result).isNotNull()
                .isExactlyInstanceOf(BoxResponse.class);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("Running Box");
        assertThat(result.price()).isEqualByComparingTo("29.99");
        assertThat(result.currency()).isEqualTo("CAD");

        verify(boxRepository).findById(boxId);
        verify(boxPriceRepository).findCurrentPrice(
                eq(boxId), any(OffsetDateTime.class));


    }
}