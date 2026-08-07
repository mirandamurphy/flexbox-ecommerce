package com.flexbox.backend.order;

import com.flexbox.backend.payment.Payment;
import com.flexbox.backend.subscription.SubscriptionPlan;
import com.flexbox.backend.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.generator.EventType;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "checkout_session", schema = "public", uniqueConstraints = {@UniqueConstraint(name = "checkout_session_stripe_session_id_key",
        columnNames = {"stripe_session_id"})})
public class CheckoutSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "checkout_session_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "stripe_session_id", length = Integer.MAX_VALUE)
    private String stripeSessionId;

    // Nullable: a checkout session represents either a single subscription
    // plan purchase, or a cart-based multi-item order (subscriptionPlan is
    // null in that case). Matches the database column, which was already
    // nullable before this branch was merged.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_plan_id")
    private SubscriptionPlan subscriptionPlan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @Column(name = "amount_subtotal", precision = 7, scale = 2)
    private BigDecimal amountSubtotal;

    @Column(name = "amount_tax", precision = 7, scale = 2)
    private BigDecimal amountTax;

    @Column(name = "amount_total", precision = 7, scale = 2)
    private BigDecimal amountTotal;

    @ColumnDefault("'CAD'")
    @Column(name = "currency", nullable = false, length = 3)
    private String currency;

    @Column(name = "success_url", length = Integer.MAX_VALUE)
    private String successUrl;

    @Column(name = "cancel_url", length = Integer.MAX_VALUE)
    private String cancelUrl;

    @Column(name = "expires_at")
    private OffsetDateTime expiresAt;

    @Column(name = "completed_at")
    private OffsetDateTime completedAt;

    @Generated(event = EventType.INSERT)
    @ColumnDefault("now()")
    @Column(name = "created_at", insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Generated(event = {EventType.INSERT, EventType.UPDATE})
    @Column(name = "updated_at", insertable = false, updatable = false)
    private OffsetDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "mode", columnDefinition = "checkout_session_mode not null")
    private CheckoutSessionMode mode;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "status", columnDefinition = "checkout_session_status not null")
    private CheckoutSessionStatus status;
}
