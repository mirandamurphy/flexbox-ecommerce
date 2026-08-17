package com.flexbox.backend.catalog.box.repository;

import com.flexbox.backend.catalog.box.model.SubscriptionBoxProduct;
import com.flexbox.backend.catalog.box.model.SubscriptionBoxProductId;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SubscriptionBoxProductRepository extends JpaRepository<SubscriptionBoxProduct, SubscriptionBoxProductId> {


}