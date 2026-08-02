package com.flexbox.backend.marketing;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "newsletter", schema = "public")
public class Newsletter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "newsletter_id", nullable = false)
    private Long id;

    @Column(name = "name", length = Integer.MAX_VALUE)
    private String name;

    @Column(name = "subject", length = Integer.MAX_VALUE)
    private String subject;

    @Column(name = "html_file", length = Integer.MAX_VALUE)
    private String htmlFile;

    @ColumnDefault("now()")
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "type", columnDefinition = "newsletter_type not null")
    private Object type;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;


}