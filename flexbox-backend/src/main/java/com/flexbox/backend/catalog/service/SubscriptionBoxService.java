package com.flexbox.backend.catalog.service;

import com.flexbox.backend.catalog.dto.subscriptionbox.SubscriptionBoxDetail;
import com.flexbox.backend.catalog.dto.subscriptionbox.SubscriptionBoxSummary;
import com.flexbox.backend.catalog.repository.SubscriptionBoxRepository;
import com.flexbox.backend.catalog.response.SubscriptionBoxListResponse;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class SubscriptionBoxService {

    private final SubscriptionBoxRepository subscriptionBoxRepository;

    SubscriptionBoxService(SubscriptionBoxRepository subscriptionBoxRepository) {
        this.subscriptionBoxRepository = subscriptionBoxRepository;
    }

    public SubscriptionBoxListResponse getAllSubscriptionBoxes() {
        List<SubscriptionBoxSummary> subscriptionBoxes = subscriptionBoxRepository.findAll()
                .stream()
                .map(box -> SubscriptionBoxSummary.from(
                        box,
                        )
                .toList();
        return new SubscriptionBoxSummary(subscriptionBoxes);
    }
}
