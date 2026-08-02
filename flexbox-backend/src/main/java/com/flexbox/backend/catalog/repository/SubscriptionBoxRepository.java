package com.flexbox.backend.catalog.repository;

import com.flexbox.backend.catalog.entity.SubscriptionBox;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubscriptionBoxRepository extends JpaRepository<SubscriptionBox, Long> {

    @EntityGraph(attributePaths = {"subscription_box_price", "subscription_box_product"})
    List<SubscriptionBox> findAll();



    // GET /subscription-boxes?name=
    Optional<SubscriptionBox> findByNameIgnoreCase(String name, Sort sort, Limit limit);

    // GET /admin/analytics
    List<SubscriptionBox> findAllByAvailableUnitsGreaterThan(Integer availableUnitsIsGreaterThan, Sort sort, Limit limit);



}