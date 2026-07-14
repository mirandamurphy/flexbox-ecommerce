package com.flexbox.backend.subscription;

import com.flexbox.backend.user.User;
import com.flexbox.backend.address.Address;
import com.flexbox.backend.catalog.SubscriptionBox;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "subscription_plan", schema = "public", uniqueConstraints = {@UniqueConstraint(name = "subscription_plan_stripe_subscription_id_key",
        columnNames = {"stripe_subscription_id"})})
public class SubscriptionPlan {
    @Id
    @Column(name = "subscription_plan_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "plan_name", length = Integer.MAX_VALUE)
    private String planName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_box_id")
    private SubscriptionBox subscriptionBox;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipping_address_id")
    private Address shippingAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "billing_address_id")
    private Address billingAddress;

    @Column(name = "stripe_subscription_id", length = Integer.MAX_VALUE)
    private String stripeSubscriptionId;

    @Column(name = "current_plan_start")
    private OffsetDateTime currentPlanStart;

    @Column(name = "current_plan_end")
    private OffsetDateTime currentPlanEnd;

    @Column(name = "cancel_at_period_end")
    private Boolean cancelAtPeriodEnd;

    @Column(name = "cancelled_at")
    private OffsetDateTime cancelledAt;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @Column(name = "status", columnDefinition = "subscription_plan_status")
    private Object status;


}