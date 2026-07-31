package com.flexbox.backend.address;

import com.flexbox.backend.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "address", schema = "public")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "unit_no", length = Integer.MAX_VALUE)
    private String unitNo;

    @Column(name = "civic_no", length = Integer.MAX_VALUE)
    private String civicNo;

    @Column(name = "street", length = Integer.MAX_VALUE)
    private String street;

    @Column(name = "po_box_number", length = Integer.MAX_VALUE)
    private String poBoxNumber;

    @Column(name = "city", length = Integer.MAX_VALUE)
    private String city;

    @Column(name = "province", columnDefinition = "provinces")
    private Object province;

    @Column(name = "postal_code", length = 7)
    private String postalCode;

    @ColumnDefault("'CA'")
    @Column(name = "country", length = 2)
    private String country;

    @Column(name = "type", nullable = false)
    private AddressType type;

    @ColumnDefault("false")
    @Column(name = "is_default", nullable = false)
    private Boolean isDefault;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;


}