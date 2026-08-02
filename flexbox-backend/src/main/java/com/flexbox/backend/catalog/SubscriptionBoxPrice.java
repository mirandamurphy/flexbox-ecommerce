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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subscription_box_price_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "subscription_box_id", nullable = false)
    private SubscriptionBox subscriptionBox;

    @Column(name = "amount", nullable = false, precision = 5, scale = 2)
    private BigDecimal amount;

    @ColumnDefault("'CAD'")
    @Column(name = "currency", nullable = false, length = 3)
    private String currency;

    @Column(name = "starts_at", nullable = false)
    private OffsetDateTime startsAt;

    @Column(name = "ends_at")
    private OffsetDateTime endsAt;

    @Column(name = "stripe_price_id", nullable = false, length = Integer.MAX_VALUE)
    private String stripePriceId;


}