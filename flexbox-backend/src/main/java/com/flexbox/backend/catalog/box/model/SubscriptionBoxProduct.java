package com.flexbox.backend.catalog.box.model;

import com.flexbox.backend.catalog.product.model.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "subscription_box_product", schema = "public")
public class SubscriptionBoxProduct {
    @EmbeddedId
    private SubscriptionBoxProductId id;

    @MapsId("subscriptionBoxId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "subscription_box_id", nullable = false)
    private SubscriptionBox subscriptionBox;

    @MapsId("productId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ColumnDefault("1")
    @Column(name = "quantity", nullable = false)
    private Integer quantity;


}