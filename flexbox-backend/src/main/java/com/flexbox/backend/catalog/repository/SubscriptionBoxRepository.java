package com.flexbox.backend.catalog.repository;

import com.flexbox.backend.catalog.entity.SubscriptionBox;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubscriptionBoxRepository extends JpaRepository<SubscriptionBox, Long> {

    // GET /subscription-boxes?name=
    Optional<SubscriptionBox> findByNameIgnoreCase(String name, Sort sort, Limit limit);
    List<SubscriptionBox> findAllByNameContainingIgnoreCase(String name, Sort sort, Limit limit);

    // GET /subscription-boxes?description=
    List<SubscriptionBox> findAllByDescriptionContainingIgnoreCase(String description, Sort sort, Limit limit);

    // GET /admin/analytics
    List<SubscriptionBox> findAllByAvailableUnitsGreaterThan(Integer availableUnitsIsGreaterThan, Sort sort, Limit limit);



}