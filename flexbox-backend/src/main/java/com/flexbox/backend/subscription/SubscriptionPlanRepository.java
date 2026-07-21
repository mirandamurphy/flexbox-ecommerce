package com.flexbox.backend.subscription;

import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, Long> {

    // GET /admin/analytics - find total number of active/canceled subs
    List<SubscriptionPlan> findAllByStatusIs(SubscriptionPlanStatus status, Sort sort, Limit limit);
    List<SubscriptionPlan> findAllByPlanNameIgnoreCase(String planName, Sort sort, Limit limit);
}