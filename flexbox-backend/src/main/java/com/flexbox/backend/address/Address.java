package com.flexbox.backend.address;

import com.flexbox.backend.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "address", schema = "public", indexes = {@Index(name = "address_user_id_type_idx",
        columnList = "user_id, type",
        unique = true)})
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "unit_no", length = Integer.MAX_VALUE)
    private String unitNo;

    @Column(name = "civic_no", length = Integer.MAX_VALUE)
    private String civicNo;

    @Column(name = "street", length = Integer.MAX_VALUE)
    private String street;

    @Column(name = "po_box_number", length = Integer.MAX_VALUE)
    private String poBoxNumber;

    @Column(name = "city", nullable = false, length = Integer.MAX_VALUE)
    private String city;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "province", columnDefinition = "provinces not null")
    private Province province;

    @Column(name = "postal_code", nullable = false, length = 7)
    private String postalCode;

    @ColumnDefault("'CA'")
    @Column(name = "country", nullable = false, length = 2)
    private String country;

    @ColumnDefault("true")
    @Column(name = "is_default", nullable = false)
    private Boolean isDefault;

    @ColumnDefault("true")
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @ColumnDefault("now()")
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "type", columnDefinition = "address_type not null")
    private AddressType type;


}