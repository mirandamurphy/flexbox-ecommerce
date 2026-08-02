package com.flexbox.backend.webhook;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stripe_webhook_event_id", nullable = false)
    private Long id;

    @Column(name = "stripe_event_id", nullable = false, length = Integer.MAX_VALUE)
    private String stripeEventId;

    @Column(name = "event_type", nullable = false, length = Integer.MAX_VALUE)
    private String eventType;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "payload", nullable = false)
    private Map<String, Object> payload;

    @ColumnDefault("false")
    @Column(name = "is_processed", nullable = false)
    private Boolean isProcessed;

    @ColumnDefault("now()")
    @Column(name = "received_at", nullable = false)
    private OffsetDateTime receivedAt;

    @Column(name = "processed_at")
    private OffsetDateTime processedAt;


}