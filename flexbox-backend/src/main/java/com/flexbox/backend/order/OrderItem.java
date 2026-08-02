package com.flexbox.backend.order;

import com.flexbox.backend.catalog.entity.SubscriptionBox;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "order_item", schema = "public")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "subscription_box_id", nullable = false)
    private SubscriptionBox subscriptionBox;

    @Column(name = "subscription_box_name_snapshot", nullable = false, length = Integer.MAX_VALUE)
    private String subscriptionBoxNameSnapshot;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "purchase_price_snapshot", nullable = false, precision = 5, scale = 2)
    private BigDecimal purchasePriceSnapshot;


}