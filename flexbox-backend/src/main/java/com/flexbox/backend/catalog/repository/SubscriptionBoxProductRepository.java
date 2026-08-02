package com.flexbox.backend.catalog.repository;

import com.flexbox.backend.catalog.entity.SubscriptionBoxProduct;
import com.flexbox.backend.catalog.entity.SubscriptionBoxProductId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionBoxProductRepository extends JpaRepository<SubscriptionBoxProduct, SubscriptionBoxProductId> {

    List<SubscriptionBoxProduct> findAllBySubscriptionBoxId(Long id);
}