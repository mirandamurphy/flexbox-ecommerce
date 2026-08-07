package com.flexbox.backend.catalog;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "product_inventory", schema = "public")
public class ProductInventory {
    @Id
    @Column(name = "product_id", nullable = false)
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "in_stock")
    private Integer inStock;

    @Column(name = "reserved")
    private Integer reserved;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;


}