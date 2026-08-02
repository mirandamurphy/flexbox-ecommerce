package com.flexbox.backend.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "tokens", schema = "public", uniqueConstraints = {@UniqueConstraint(name = "tokens_token_value_key",
        columnNames = {"token_value"})})
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "token_value", nullable = false, length = Integer.MAX_VALUE)
    private String tokenValue;

    @ColumnDefault("false")
    @Column(name = "is_revoked", nullable = false)
    private Boolean isRevoked;

    @ColumnDefault("now()")
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "expired_at")
    private OffsetDateTime expiredAt;

    @Column(name = "type", columnDefinition = "token_type not null")
    private Object type;


}