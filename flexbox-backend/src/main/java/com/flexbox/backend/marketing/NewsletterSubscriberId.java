package com.flexbox.backend.marketing;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@Embeddable
public class NewsletterSubscriberId implements Serializable {
    @Serial
    private static final long serialVersionUID = -7314501497996409168L;
    @Column(name = "newsletter_id", nullable = false)
    private Long newsletterId;

    @Column(name = "user_id", nullable = false)
    private Long userId;


}