package com.flexbox.backend.order;

import com.flexbox.backend.catalog.SubscriptionBox;
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
    @Column(name = "order_item_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_box_id")
    private SubscriptionBox subscriptionBox;

    @Column(name = "subscription_box_name_snapshot", length = Integer.MAX_VALUE)
    private String subscriptionBoxNameSnapshot;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "purchase_price_snapshot", precision = 5, scale = 2)
    private BigDecimal purchasePriceSnapshot;


}