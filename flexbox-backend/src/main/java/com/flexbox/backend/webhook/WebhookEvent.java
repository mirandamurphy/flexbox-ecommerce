package com.flexbox.backend.webhook;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.Map;

@Getter
@Setter
@Entity
@Table(name = "webhook_event", schema = "public", uniqueConstraints = {@UniqueConstraint(name = "webhook_event_stripe_event_id_key",
        columnNames = {"stripe_event_id"})})
public class WebhookEvent {
    @Id
    @Column(name = "stripe_webhook_event_id", nullable = false)
    private Long id;

    @Column(name = "stripe_event_id", length = Integer.MAX_VALUE)
    private String stripeEventId;

    @Column(name = "event_type", length = Integer.MAX_VALUE)
    private String eventType;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "payload")
    private Map<String, Object> payload;

    @Column(name = "is_processed")
    private Boolean isProcessed;

    @Column(name = "received_at")
    private OffsetDateTime receivedAt;

    @Column(name = "processed_at")
    private OffsetDateTime processedAt;


}