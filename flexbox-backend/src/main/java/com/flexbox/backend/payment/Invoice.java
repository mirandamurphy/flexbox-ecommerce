package com.flexbox.backend.payment;

import com.flexbox.backend.subscription.SubscriptionPlan;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "invoice", schema = "public", uniqueConstraints = {@UniqueConstraint(name = "invoice_stripe_invoice_id_key",
        columnNames = {"stripe_invoice_id"})})
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invoice_id", nullable = false)
    private Long id;

    @Column(name = "stripe_invoice_id", length = Integer.MAX_VALUE)
    private String stripeInvoiceId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "subscription_plan_id", nullable = false)
    private SubscriptionPlan subscriptionPlan;

    @Column(name = "amount_due", nullable = false, precision = 7, scale = 2)
    private BigDecimal amountDue;

    @ColumnDefault("'CAD'")
    @Column(name = "currency", length = 3)
    private String currency;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "paid_at")
    private OffsetDateTime paidAt;

    @Column(name = "status", columnDefinition = "invoice_status not null")
    private Object status;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;


}