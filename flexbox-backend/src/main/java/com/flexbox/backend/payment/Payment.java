package com.flexbox.backend.payment;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

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

    @Column(name = "status", columnDefinition = "payment_status not null")
    private Object status;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @ColumnDefault("now()")
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;


}