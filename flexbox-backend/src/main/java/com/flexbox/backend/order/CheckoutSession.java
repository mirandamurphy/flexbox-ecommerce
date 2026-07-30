package com.flexbox.backend.order;

import com.flexbox.backend.payment.Payment;
import com.flexbox.backend.subscription.SubscriptionPlan;
import com.flexbox.backend.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "checkout_session", schema = "public", uniqueConstraints = {@UniqueConstraint(name = "checkout_session_stripe_session_id_key",
        columnNames = {"stripe_session_id"})})
public class CheckoutSession {
    @Id
    @Column(name = "checkout_session_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "stripe_session_id", length = Integer.MAX_VALUE)
    private String stripeSessionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_plan_id")
    private SubscriptionPlan subscriptionPlan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @Column(name = "mode", columnDefinition = "checkout_session_mode")
    private CheckoutSessionMode mode;

    @Column(name = "status", columnDefinition = "checkout_session_status")
    private CheckoutSessionStatus status;

    @Column(name = "amount_subtotal", precision = 7, scale = 2)
    private BigDecimal amountSubtotal;

    @Column(name = "amount_tax", precision = 7, scale = 2)
    private BigDecimal amountTax;

    @Column(name = "amount_total", precision = 7, scale = 2)
    private BigDecimal amountTotal;

    @Column(name = "currency", length = 3)
    private String currency;

    @Column(name = "success_url", length = Integer.MAX_VALUE)
    private String successUrl;

    @Column(name = "cancel_url", length = Integer.MAX_VALUE)
    private String cancelUrl;

    @Column(name = "expires_at")
    private OffsetDateTime expiresAt;

    @Column(name = "completed_at")
    private OffsetDateTime completedAt;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;


}