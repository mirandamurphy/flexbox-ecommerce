package com.flexbox.backend.marketing;

import com.flexbox.backend.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "newsletter_subscribers", schema = "public")
public class NewsletterSubscriber {
    @EmbeddedId
    private NewsletterSubscriberId id;

    @MapsId("newsletterId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "newsletter_id", nullable = false)
    private Newsletter newsletter;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "sent_at")
    private OffsetDateTime sentAt;


}