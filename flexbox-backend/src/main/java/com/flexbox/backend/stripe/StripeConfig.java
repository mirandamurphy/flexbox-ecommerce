package com.flexbox.backend.stripe;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class StripeConfig {

    private final String secretKey;

    public StripeConfig(@Value("${stripe.api.secret-key}") String secretKey) {
        this.secretKey = secretKey;
    }

    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
    }
}
