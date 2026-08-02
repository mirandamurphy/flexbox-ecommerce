package com.flexbox.backend.catalog.service;

import com.flexbox.backend.catalog.dto.subscriptionbox.SubscriptionBoxDetail;
import com.flexbox.backend.catalog.dto.subscriptionbox.SubscriptionBoxSummary;
import com.flexbox.backend.catalog.repository.SubscriptionBoxRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class SubscriptionBoxService {

    private final SubscriptionBoxRepository subscriptionBoxRepository;

    SubscriptionBoxService(SubscriptionBoxRepository subscriptionBoxRepository) {
        this.subscriptionBoxRepository = subscriptionBoxRepository;
    }

    public SubscriptionBoxSummary getAllSubscriptionBoxes() {
        List<SubscriptionBoxDetail> subscriptionBoxes = subscriptionBoxRepository.findAll()
                .stream()
                .map(SubscriptionBoxDetail::from)
                .toList();
        return new SubscriptionBoxSummary(subscriptionBoxes);
    }
}
