package com.flexbox.backend.admin.model;

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
public class ViewActiveSubscriptionBoxProductId implements Serializable {
    private static final long serialVersionUID = -5902426328315162321L;
    @Column(name = "subscription_box_id")
    private Long subscriptionBoxId;

    @Column(name = "product_id")
    private Long productId;


}