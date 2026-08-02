package com.flexbox.backend.cart;

import com.flexbox.backend.catalog.SubscriptionBox;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "cart_item", schema = "public")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_item_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "subscription_box_id", nullable = false)
    private SubscriptionBox subscriptionBox;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "unit_price_snapshot", nullable = false, precision = 5, scale = 2)
    private BigDecimal unitPriceSnapshot;

    @ColumnDefault("now()")
    @Column(name = "added_at", nullable = false)
    private OffsetDateTime addedAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;


}