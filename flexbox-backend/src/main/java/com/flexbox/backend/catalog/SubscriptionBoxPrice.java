package com.flexbox.backend.catalog;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "subscription_box_price", schema = "public", uniqueConstraints = {@UniqueConstraint(name = "subscription_box_price_stripe_price_id_key",
        columnNames = {"stripe_price_id"})})
public class SubscriptionBoxPrice {
    @Id
    @Column(name = "subscription_box_price_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_box_id")
    private SubscriptionBox subscriptionBox;

    @Column(name = "amount", precision = 5, scale = 2)
    private BigDecimal amount;

    @ColumnDefault("'CAD'")
    @Column(name = "currency", length = 3)
    private String currency;

    @Column(name = "starts_at")
    private OffsetDateTime startsAt;

    @Column(name = "ends_at")
    private OffsetDateTime endsAt;

    @Column(name = "stripe_price_id", length = Integer.MAX_VALUE)
    private String stripePriceId;


}