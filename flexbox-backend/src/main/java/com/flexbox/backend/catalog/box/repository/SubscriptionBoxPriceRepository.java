package com.flexbox.backend.catalog.box.repository;

import com.flexbox.backend.catalog.box.model.SubscriptionBoxPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface SubscriptionBoxPriceRepository extends JpaRepository<SubscriptionBoxPrice, Long> {

    /*
    Fetches the entire SubscriptionBoxPrice entity where the price starts_at
    is before OR equal to now and the price ends_at is greater than now.
    This allows the ends_at price to be set in advance without affecting
    finding the current subscription box price.
     */
    @Query(
            """
            FROM SubscriptionBoxPrice p
            WHERE p.subscriptionBox.id = :subscriptionBoxId
            AND p.startsAt <= :now
            AND (p.endsAt IS NULL OR p.endsAt > :now)
            """
    )
    Optional<SubscriptionBoxPrice> findCurrentPrice(
            @Param("subscriptionBoxId") Long subscriptionBoxId,
            @Param ("now") OffsetDateTime now);


    @Query("""
               SELECT p FROM SubscriptionBoxPrice p
               WHERE p.subscriptionBox.id IN :boxIds
               AND p.startsAt <= :now
               AND (p.endsAt IS NULL OR p.endsAt > :now)
           
           """)
    List<SubscriptionBoxPrice> findCurrentPrices(List<Long> boxIds, OffsetDateTime now);


}