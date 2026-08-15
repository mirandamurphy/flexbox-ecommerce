package com.flexbox.backend.catalog.box.service;

import com.flexbox.backend.catalog.box.dto.subscriptionbox.BoxDetailResponse;
import com.flexbox.backend.catalog.box.dto.subscriptionbox.BoxSummaryResponse;
import com.flexbox.backend.catalog.box.repository.SubscriptionBoxPriceRepository;
import com.flexbox.backend.catalog.box.repository.SubscriptionBoxProductRepository;
import com.flexbox.backend.catalog.box.repository.SubscriptionBoxRepository;
import com.flexbox.backend.common.dto.response.CollectionResponse;
import com.flexbox.backend.common.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;



@Service
public class SubscriptionBoxService {

    private final SubscriptionBoxRepository boxRepository;
    private final SubscriptionBoxPriceRepository boxPriceRepository;
    private final SubscriptionBoxProductRepository boxProductRepository;

    SubscriptionBoxService(SubscriptionBoxRepository boxRepository, SubscriptionBoxPriceRepository boxPriceRepository, SubscriptionBoxProductRepository boxProductRepository) {
        this.boxRepository = boxRepository;
        this.boxPriceRepository = boxPriceRepository;
        this.boxProductRepository = boxProductRepository;
    }

    @Transactional(readOnly = true)
    public CollectionResponse<BoxSummaryResponse> getBoxes() {
        var boxes = boxRepository.findAll()
                .stream()
                .map(box ->
                        BoxSummaryResponse.from(
                                box,
                                boxPriceRepository
                                        .findActivePriceBySubscriptionBoxId(
                                                box.getId(),
                                                OffsetDateTime.now())
                                        .orElseThrow(() ->
                                                new ResourceNotFoundException(
                                                        "Active price not found for box ID '%d' ".formatted(box.getId())

                                                ))))
                .toList();
        return new CollectionResponse<>(boxes);
    }

    @Transactional(readOnly = true)
    public BoxDetailResponse getBoxById(Long id) {

        var box = boxRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Subscription box not found with ID '%d'".formatted(id)));

        var price = boxPriceRepository.
                findActivePriceBySubscriptionBoxId(
                        id, OffsetDateTime.now())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Active price not found for box ID '%d' ".formatted(id)));

        var products = boxProductRepository.findAllBySubscriptionBoxId(id);

        return BoxDetailResponse.from(box, price, products);

    }
}
