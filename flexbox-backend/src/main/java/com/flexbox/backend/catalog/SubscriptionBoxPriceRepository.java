package com.flexbox.backend.catalog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.Optional;

public interface SubscriptionBoxPriceRepository extends JpaRepository<SubscriptionBoxPrice, Long> {

    @Query("SELECT p FROM SubscriptionBoxPrice p " +
            "WHERE p.subscriptionBox.id = :subscriptionBoxId " +
            "AND p.startsAt <= :now AND (p.endsAt IS NULL OR p.endsAt > :now) " +
            "ORDER BY p.startsAt DESC")
    Optional<SubscriptionBoxPrice> findCurrentPrice(@Param("subscriptionBoxId") Long subscriptionBoxId,
                                                      @Param("now") OffsetDateTime now);
}
