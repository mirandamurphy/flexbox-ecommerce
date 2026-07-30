package com.flexbox.backend.order;

import com.flexbox.backend.user.User;
import com.flexbox.backend.address.Address;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "\"order\"", schema = "public")
public class Order {
    @Id
    @Column(name = "order_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipping_address_id")
    private Address shippingAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "billing_address_id")
    private Address billingAddress;

    @ColumnDefault("'CAD'")
    @Column(name = "currency", length = 3)
    private String currency;

    @Column(name = "total_amount", precision = 7, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "order_status", columnDefinition = "order_status")
    private OrderStatus orderStatus;

    @Column(name = "order_date")
    private OffsetDateTime orderDate;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;


}