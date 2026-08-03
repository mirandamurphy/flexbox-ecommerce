package com.flexbox.backend.payment;

import com.flexbox.backend.order.Order;
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
@Table(name = "payment", schema = "public", uniqueConstraints = {@UniqueConstraint(name = "payment_stripe_payment_intent_id_key",
        columnNames = {"stripe_payment_intent_id"})})
public class Payment {
    @Id
    @Column(name = "payment_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(name = "stripe_payment_intent_id", length = Integer.MAX_VALUE)
    private String stripePaymentIntentId;

    @Column(name = "idempotency_key")
    private UUID idempotencyKey;

    @Column(name = "amount", precision = 7, scale = 2)
    private BigDecimal amount;

    @ColumnDefault("'CAD'")
    @Column(name = "currency", length = 3)
    private String currency;

    @Column(name = "paid_at")
    private OffsetDateTime paidAt;

    @Column(name = "status", columnDefinition = "payment_status")
    private Object status;


}