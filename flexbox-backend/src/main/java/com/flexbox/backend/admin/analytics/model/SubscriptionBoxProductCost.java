package com.flexbox.backend.admin.analytics.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;

@Getter
@Entity
@Immutable
@Table(name = "view_subscription_box_product_cost", schema = "public")
public class SubscriptionBoxProductCost {
    @EmbeddedId
    private SubscriptionBoxProductCostId id;

    @Column(name = "box_name")
    private String boxName;

    @Column(name = "brand")
    private String brand;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "category_name")
    private String categoryName;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "product_cost", precision = 5, scale = 2)
    private BigDecimal productCost;


}