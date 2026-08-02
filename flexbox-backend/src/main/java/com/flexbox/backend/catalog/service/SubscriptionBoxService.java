package com.flexbox.backend.catalog.service;

import com.flexbox.backend.catalog.dto.subscriptionbox.SubscriptionBoxDetail;
import com.flexbox.backend.catalog.dto.subscriptionbox.SubscriptionBoxSummary;
import com.flexbox.backend.catalog.entity.SubscriptionBoxProduct;
import com.flexbox.backend.catalog.exception.SubscriptionBoxNotFoundException;
import com.flexbox.backend.catalog.exception.SubscriptionBoxPriceNotFoundException;
import com.flexbox.backend.catalog.repository.SubscriptionBoxPriceRepository;
import com.flexbox.backend.catalog.repository.SubscriptionBoxProductRepository;
import com.flexbox.backend.catalog.repository.SubscriptionBoxRepository;
import com.flexbox.backend.catalog.response.SubscriptionBoxListResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;


@Service
public class SubscriptionBoxService {

    private final SubscriptionBoxRepository subscriptionBoxRepository;
    private final SubscriptionBoxPriceRepository subscriptionBoxPriceRepository;
    private final SubscriptionBoxProductRepository subscriptionBoxProductRepository;

    SubscriptionBoxService(SubscriptionBoxRepository subscriptionBoxRepository, SubscriptionBoxPriceRepository subscriptionBoxPriceRepository, SubscriptionBoxProductRepository subscriptionBoxProductRepository) {
        this.subscriptionBoxRepository = subscriptionBoxRepository;
        this.subscriptionBoxPriceRepository = subscriptionBoxPriceRepository;
        this.subscriptionBoxProductRepository = subscriptionBoxProductRepository;
    }

    @Transactional(readOnly = true)
    public SubscriptionBoxListResponse getAllSubscriptionBoxes() {
        List<SubscriptionBoxSummary> subscriptionBoxes = subscriptionBoxRepository.findAll()
                .stream()
                .map(box -> SubscriptionBoxSummary.from(
                        box,
                        subscriptionBoxPriceRepository
                                .findActivePriceBySubscriptionBoxId(box.getId(), OffsetDateTime.now())
                                .orElseThrow(() -> new SubscriptionBoxPriceNotFoundException("Active price not found for box ID: " + box.getId()))
                ))
                .toList();
        return new SubscriptionBoxListResponse(subscriptionBoxes);
    }

    @Transactional(readOnly = true)
    public SubscriptionBoxDetail getSubscriptionBoxById(Long id) {
        var subscriptionBox = subscriptionBoxRepository.findById(id)
                .orElseThrow(() -> new SubscriptionBoxNotFoundException("Subscription box not found with ID: " + id));
        var price = subscriptionBoxPriceRepository.findActivePriceBySubscriptionBoxId(id, OffsetDateTime.now())
                .orElseThrow(() -> new SubscriptionBoxPriceNotFoundException("Active price not found for box ID: " + id));

        List<SubscriptionBoxProduct> products = subscriptionBoxProductRepository.findAllBySubscriptionBoxId(id);

        return SubscriptionBoxDetail.from(subscriptionBox, price, products);

    }
}
