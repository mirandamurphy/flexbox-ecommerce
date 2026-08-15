package com.flexbox.backend.catalog.box.service;

import com.flexbox.backend.catalog.box.dto.subscriptionbox.BoxResponse;
import com.flexbox.backend.catalog.box.model.SubscriptionBox;
import com.flexbox.backend.catalog.box.model.SubscriptionBoxPrice;
import com.flexbox.backend.catalog.box.repository.SubscriptionBoxPriceRepository;
import com.flexbox.backend.catalog.box.repository.SubscriptionBoxRepository;
import com.flexbox.backend.common.dto.response.CollectionResponse;
import com.flexbox.backend.common.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class SubscriptionBoxService {

    private final SubscriptionBoxRepository boxRepository;
    private final SubscriptionBoxPriceRepository boxPriceRepository;

    SubscriptionBoxService(SubscriptionBoxRepository boxRepository, SubscriptionBoxPriceRepository boxPriceRepository) {
        this.boxRepository = boxRepository;
        this.boxPriceRepository = boxPriceRepository;

    }

    @Transactional(readOnly = true)
    public CollectionResponse<BoxResponse> getBoxes() {

        var boxes = boxRepository.findAllByIsActiveTrueOrderByIdAsc();

        var boxIds = boxes
                .stream()
                .map(SubscriptionBox::getId)
                .toList();

        Map<Long, SubscriptionBoxPrice> boxPriceMap = boxPriceRepository
                .findCurrentPrices(boxIds, OffsetDateTime.now())
                .stream()
                .collect(
                        Collectors.toMap(
                                price -> price.getSubscriptionBox().getId(),
                                price -> price));

        var boxResponses = boxes.stream()
                .map(
                        box -> BoxResponse.from(
                                box, Optional.ofNullable(boxPriceMap.get(box.getId()))
                                                .orElseThrow(() -> new ResourceNotFoundException(
                                                        "Active price not found for box ID '%d'".formatted(box.getId())
                                                ))))
                                .toList();

        return new CollectionResponse<>(boxResponses);
    }
    @Transactional(readOnly = true)
    public BoxResponse getBoxById(Long id) {

        var box = boxRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Subscription box not found with ID '%d'".formatted(id)));

        var price = boxPriceRepository.
                findCurrentPrice(
                        id, OffsetDateTime.now())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Active price not found for box ID '%d'".formatted(id)));


        return BoxResponse.from(box, price);

    }
}
