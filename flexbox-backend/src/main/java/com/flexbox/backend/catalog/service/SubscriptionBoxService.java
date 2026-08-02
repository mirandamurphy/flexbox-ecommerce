package com.flexbox.backend.catalog.service;

import com.flexbox.backend.catalog.dto.subscriptionbox.SubscriptionBoxSummary;
import com.flexbox.backend.catalog.exception.SubscriptionBoxPriceNotFoundException;
import com.flexbox.backend.catalog.repository.SubscriptionBoxPriceRepository;
import com.flexbox.backend.catalog.repository.SubscriptionBoxRepository;
import com.flexbox.backend.catalog.response.SubscriptionBoxListResponse;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class SubscriptionBoxService {

    private final SubscriptionBoxRepository subscriptionBoxRepository;
    private final SubscriptionBoxPriceRepository subscriptionBoxPriceRepository;

    SubscriptionBoxService(SubscriptionBoxRepository subscriptionBoxRepository, SubscriptionBoxPriceRepository subscriptionBoxPriceRepository) {
        this.subscriptionBoxRepository = subscriptionBoxRepository;
        this.subscriptionBoxPriceRepository = subscriptionBoxPriceRepository;
    }

    public SubscriptionBoxListResponse getAllSubscriptionBoxes() {
        List<SubscriptionBoxSummary> subscriptionBoxes = subscriptionBoxRepository.findAll()
                .stream()
                .map(box -> SubscriptionBoxSummary.from(
                        box,
                                subscriptionBoxPriceRepository
                                        .findActivePriceBySubscriptionBoxId(box.getId(), OffsetDateTime.now())
                                        .orElseThrow(() -> new SubscriptionBoxPriceNotFoundException("Active price not found for box " + box.getId()))
                        ))
                .toList();
        return new SubscriptionBoxListResponse(subscriptionBoxes);
    }
}
