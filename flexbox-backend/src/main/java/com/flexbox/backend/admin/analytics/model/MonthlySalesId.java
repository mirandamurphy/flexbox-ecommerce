package com.flexbox.backend.admin.analytics.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;

@Getter
@Setter
@EqualsAndHashCode
@Embeddable
public class MonthlySalesId implements Serializable {
    @Serial
    private static final long serialVersionUID = -3157370976265136046L;
    @Column(name = "month")
    private OffsetDateTime month;

    @Column(name = "subscription_box_id")
    private Long subscriptionBoxId;

    protected MonthlySalesId() {}

    public MonthlySalesId(OffsetDateTime month, Long boxId) {
        this.month = month;
        this.subscriptionBoxId = boxId;
    }
}