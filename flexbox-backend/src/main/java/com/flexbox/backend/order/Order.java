package com.flexbox.backend.order;

import com.flexbox.backend.address.Address;
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
@Table(name = "\"order\"", schema = "public")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Made optional: not every checkout flow collects a shipping address at
    // order-creation time (e.g. cart-based checkout does not yet). Required
    // by the subscription-plan checkout flow, not by the cart-based one.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipping_address_id")
    private Address shippingAddress;

    // Same reasoning as shippingAddress above.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "billing_address_id")
    private Address billingAddress;

    @ColumnDefault("'CAD'")
    @Column(name = "currency", nullable = false, length = 3)
    private String currency;

    @Column(name = "total_amount", nullable = false, precision = 7, scale = 2)
    private BigDecimal totalAmount;

    @Generated(event = EventType.INSERT)
    @ColumnDefault("now()")
    @Column(name = "order_date", insertable = false, updatable = false)
    private OffsetDateTime orderDate;

    @Generated(event = {EventType.INSERT, EventType.UPDATE})
    @Column(name = "updated_at", insertable = false, updatable = false)
    private OffsetDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @ColumnDefault("'PENDING'")
    @Column(name = "status", columnDefinition = "order_status not null")
    private OrderStatus status;
}
