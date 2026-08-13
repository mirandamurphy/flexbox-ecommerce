package com.flexbox.backend.admin.analytics.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;

@Getter
@Entity
@Immutable
@Table(name = "view_subscription_box_cost", schema = "public")
public class SubscriptionBoxCost {
    @Id
    @Column(name = "subscription_box_id")
    private Long subscriptionBoxId;

    @Column(name = "box_name")
    private String boxName;

    @Column(name = "box_cost", precision = 5, scale = 2)
    private BigDecimal boxCost;


}