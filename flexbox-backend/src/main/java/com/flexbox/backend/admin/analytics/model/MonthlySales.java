package com.flexbox.backend.admin.analytics.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;

@Getter
@Entity
@Immutable
@Table(name = "view_monthly_sales", schema = "public")
public class MonthlySales {

    @EmbeddedId
    private MonthlySalesId id;

    @Column(name = "box_name")
    private String boxName;

    @Column(name = "units_sold")
    private Long unitsSold;

    @Column(name = "gross_revenue")
    private BigDecimal grossRevenue;

    @Column(name = "product_cost")
    private BigDecimal productCost;

    @Column(name = "gross_profit")
    private BigDecimal grossProfit;

    protected MonthlySales() {}

    public MonthlySales(
            MonthlySalesId id,
            String boxName,
            Long unitsSold,
            BigDecimal grossRevenue,
            BigDecimal productCost,
            BigDecimal grossProfit) {

        this.id = id;
        this.boxName = boxName;
        this.unitsSold = unitsSold;
        this.grossRevenue = grossRevenue;
        this.productCost = productCost;
        this.grossProfit = grossProfit;
    }


}