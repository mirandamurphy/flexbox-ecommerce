package com.flexbox.backend.catalog.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@Embeddable
public class SubscriptionBoxProductId implements Serializable {
    private static final long serialVersionUID = -8281176918313804662L;
    @Column(name = "subscription_box_id", nullable = false)
    private Long subscriptionBoxId;

    @Column(name = "product_id", nullable = false)
    private Long productId;


}