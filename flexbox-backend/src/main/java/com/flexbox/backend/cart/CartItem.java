package com.flexbox.backend.cart;

import com.flexbox.backend.catalog.SubscriptionBox;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "cart_item", schema = "public")
public class CartItem {
    @Id
    @Column(name = "cart_item_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_box_id")
    private SubscriptionBox subscriptionBox;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "unit_price_snapshot", precision = 5, scale = 2)
    private BigDecimal unitPriceSnapshot;

    @Column(name = "added_at")
    private OffsetDateTime addedAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;


}