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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_plan_id")
    private SubscriptionPlan subscriptionPlan;

    @Column(name = "amount_due", precision = 7, scale = 2)
    private BigDecimal amountDue;

    @ColumnDefault("'CAD'")
    @Column(name = "currency", length = 3)
    private String currency;

    @Column(name = "status", length = Integer.MAX_VALUE)
    private String status;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "paid_at")
    private OffsetDateTime paidAt;


}