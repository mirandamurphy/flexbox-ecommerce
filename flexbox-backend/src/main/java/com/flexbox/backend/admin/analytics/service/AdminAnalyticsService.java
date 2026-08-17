package com.flexbox.backend.admin.analytics.service;

import com.flexbox.backend.admin.analytics.dto.MonthlySalesResponse;
import com.flexbox.backend.admin.analytics.dto.SubscriptionBoxCostResponse;
import com.flexbox.backend.admin.analytics.dto.SubscriptionBoxProductCostResponse;
import com.flexbox.backend.admin.analytics.repository.MonthlySalesRepository;
import com.flexbox.backend.admin.analytics.repository.SubscriptionBoxCostRepository;
import com.flexbox.backend.admin.analytics.repository.SubscriptionBoxProductCostRepository;
import com.flexbox.backend.common.dto.response.CollectionResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminAnalyticsService {

    private final SubscriptionBoxProductCostRepository boxProductCostRepository;
    private final SubscriptionBoxCostRepository boxCostRepository;
    private final MonthlySalesRepository monthlySalesRepository;

    public AdminAnalyticsService(SubscriptionBoxProductCostRepository boxProductCostRepository, SubscriptionBoxCostRepository boxCostRepository, MonthlySalesRepository monthlySalesRepository) {
        this.boxProductCostRepository = boxProductCostRepository;
        this.boxCostRepository = boxCostRepository;
        this.monthlySalesRepository = monthlySalesRepository;
    }

    @Transactional(readOnly = true)
    public CollectionResponse<SubscriptionBoxCostResponse> getBoxCosts() {

        var boxCosts = boxCostRepository.findAll()
                .stream()
                .map(SubscriptionBoxCostResponse::from)
                .toList();

        return new CollectionResponse<>(boxCosts);
    }

    @Transactional(readOnly = true)
    public CollectionResponse<SubscriptionBoxProductCostResponse> getBoxProductCosts() {
        var boxProductCosts = boxProductCostRepository.findAll()
                .stream()
                .map(SubscriptionBoxProductCostResponse::from)
                .toList();

        return new CollectionResponse<>(boxProductCosts);
    }

    @Transactional(readOnly = true)
    public CollectionResponse<SubscriptionBoxProductCostResponse> getBoxProductCostByBoxId(Long boxId) {
        var boxProductCosts = boxProductCostRepository.findById_SubscriptionBoxId(boxId)
                .stream()
                .map(SubscriptionBoxProductCostResponse::from)
                .toList();

        return new CollectionResponse<>(boxProductCosts);
    }

    @Transactional(readOnly = true)
    public CollectionResponse<MonthlySalesResponse> getMonthlySales() {
        var monthlySales = monthlySalesRepository.findAll()
                .stream()
                .map(MonthlySalesResponse::from)
                .toList();
        return new CollectionResponse<>(monthlySales);
    }


}
