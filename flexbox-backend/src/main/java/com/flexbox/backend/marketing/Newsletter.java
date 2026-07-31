package com.flexbox.backend.marketing;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

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

    @Column(name = "type", length = Integer.MAX_VALUE)
    private String type;

    @Column(name = "created_date")
    private OffsetDateTime createdDate;


}