package com.flexbox.backend.payment;

import com.flexbox.backend.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "payment_method", schema = "public", uniqueConstraints = {@UniqueConstraint(name = "payment_method_stripe_payment_method_id_key",
        columnNames = {"stripe_payment_method_id"})})
public class PaymentMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_method_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "stripe_payment_method_id", length = Integer.MAX_VALUE)
    private String stripePaymentMethodId;

    @ColumnDefault("'card'")
    @Column(name = "type", length = Integer.MAX_VALUE)
    private String type;

    @Column(name = "last_4_digits", length = 4)
    private String last4Digits;

    @Column(name = "expiration_month")
    private Integer expirationMonth;

    @Column(name = "expiration_year")
    private Integer expirationYear;

    @Column(name = "is_default")
    private Boolean isDefault;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;


}