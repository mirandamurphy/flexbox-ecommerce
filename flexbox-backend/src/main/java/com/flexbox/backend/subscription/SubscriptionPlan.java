package com.flexbox.backend.subscription;

import com.flexbox.backend.catalog.model.SubscriptionBox;
import com.flexbox.backend.user.User;
import com.flexbox.backend.address.Address;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "subscription_plan", schema = "public", uniqueConstraints = {@UniqueConstraint(name = "subscription_plan_stripe_subscription_id_key",
        columnNames = {"stripe_subscription_id"})})
public class SubscriptionPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subscription_plan_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "plan_name", nullable = false, length = Integer.MAX_VALUE)
    private String planName;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "subscription_box_id", nullable = false)
    private SubscriptionBox subscriptionBox;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "shipping_address_id", nullable = false)
    private Address shippingAddress;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "billing_address_id", nullable = false)
    private Address billingAddress;

    @Column(name = "stripe_subscription_id", length = Integer.MAX_VALUE)
    private String stripeSubscriptionId;

    @Column(name = "current_plan_start", nullable = false)
    private OffsetDateTime currentPlanStart;

    @Column(name = "current_plan_end", nullable = false)
    private OffsetDateTime currentPlanEnd;

    @ColumnDefault("false")
    @Column(name = "cancel_at_period_end", nullable = false)
    private Boolean cancelAtPeriodEnd;

    @Column(name = "cancelled_at")
    private OffsetDateTime cancelledAt;

    @ColumnDefault("now()")
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "status", columnDefinition = "subscription_plan_status not null")
    private SubscriptionPlanStatus status;


}