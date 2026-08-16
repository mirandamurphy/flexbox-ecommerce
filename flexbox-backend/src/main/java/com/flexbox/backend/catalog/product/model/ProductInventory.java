package com.flexbox.backend.catalog.product.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;

import java.time.OffsetDateTime;

@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "product_inventory", schema = "public", uniqueConstraints = {@UniqueConstraint(name = "product_inventory_product_id_key",
        columnNames = {"product_id"})})
@NoArgsConstructor
public class ProductInventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventory_id", nullable = false)
    private Long id;

    @Column(name = "in_stock", nullable = false)
    private Integer inStock;

    @ColumnDefault("0")
    @Column(name = "reserved", nullable = false)
    private Integer reserved;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Generated(event = EventType.INSERT)
    @ColumnDefault("now()")
    @Column(name = "created_at", insertable = false, updatable = false)
    private OffsetDateTime createdAt;


}