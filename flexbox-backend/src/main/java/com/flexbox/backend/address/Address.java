package com.flexbox.backend.address;

import com.flexbox.backend.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.dialect.type.PostgreSQLEnumJdbcType;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "address", schema = "public")
public class Address {
    @Id
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

    @Column(name = "city", length = Integer.MAX_VALUE, nullable = false)
    private String city;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "province", columnDefinition = "provinces", nullable = false)
    private Object province;

    @Column(name = "postal_code", length = 7, nullable = false)
    private String postalCode;

    @ColumnDefault("'CA'")
    @Column(name = "country", length = 2, nullable = false)
    private String country;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(columnDefinition = "address_type", nullable = false)
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