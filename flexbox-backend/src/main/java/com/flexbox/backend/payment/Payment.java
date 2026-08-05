package com.flexbox.backend.payment;

import com.flexbox.backend.order.Order;
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
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "payment", schema = "public", uniqueConstraints = {
        @UniqueConstraint(name = "payment_stripe_payment_intent_id_key",
                columnNames = {"stripe_payment_intent_id"}),
        @UniqueConstraint(name = "idempotency_key_key",
                columnNames = {"idempotency_key"})})
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(name = "stripe_payment_intent_id", length = Integer.MAX_VALUE)
    private String stripePaymentIntentId;

    @Column(name = "idempotency_key")
    private UUID idempotencyKey;

    @Column(name = "amount", nullable = false, precision = 7, scale = 2)
    private BigDecimal amount;

    @ColumnDefault("'CAD'")
    @Column(name = "currency", nullable = false, length = 3)
    private String currency;

    @Column(name = "paid_at")
    private OffsetDateTime paidAt;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "status", columnDefinition = "payment_status not null")
    private PaymentStatus status;

    @Generated(event = {EventType.INSERT, EventType.UPDATE})
    @Column(name = "updated_at", insertable = false, updatable = false)
    private OffsetDateTime updatedAt;

    @Generated(event = EventType.INSERT)
    @ColumnDefault("now()")
    @Column(name = "created_at", insertable = false, updatable = false)
    private OffsetDateTime createdAt;


}