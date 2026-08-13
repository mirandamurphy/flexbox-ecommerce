package com.flexbox.backend.catalog.box.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Embeddable
@NoArgsConstructor
public class SubscriptionBoxProductId implements Serializable {
    @Serial
    private static final long serialVersionUID = -8281176918313804662L;
    @Column(name = "subscription_box_id", nullable = false)
    private Long subscriptionBoxId;

    @Column(name = "product_id", nullable = false)
    private Long productId;


}