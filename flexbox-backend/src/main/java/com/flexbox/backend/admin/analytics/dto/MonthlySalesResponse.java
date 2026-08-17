package com.flexbox.backend.admin.analytics.dto;

import com.flexbox.backend.admin.analytics.model.MonthlySales;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record MonthlySalesResponse(
        OffsetDateTime month,
        Long subscriptionBoxId,
        String boxName,
        Long unitsSold,
        BigDecimal grossRevenue,
        BigDecimal productCost,
        BigDecimal grossProfit
) {
    public static MonthlySalesResponse from(MonthlySales sales) {
        return new MonthlySalesResponse(
                sales.getId().getMonth(),
                sales.getId().getSubscriptionBoxId(),
                sales.getBoxName(),
                sales.getUnitsSold(),
                sales.getGrossRevenue(),
                sales.getProductCost(),
                sales.getGrossProfit()
        );
    }

}
