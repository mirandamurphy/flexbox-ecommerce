package com.flexbox.backend.admin.analytics.repository;

import com.flexbox.backend.admin.analytics.model.SubscriptionBoxProductCost;
import com.flexbox.backend.admin.analytics.model.SubscriptionBoxProductCostId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionBoxProductCostRepository extends JpaRepository<SubscriptionBoxProductCost, SubscriptionBoxProductCostId> {

    List<SubscriptionBoxProductCost> findById_SubscriptionBoxId(Long subscriptionBoxId);


}