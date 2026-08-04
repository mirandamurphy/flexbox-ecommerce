package com.flexbox.backend.catalog.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SubscriptionBoxPriceNotFoundException extends RuntimeException {
    public SubscriptionBoxPriceNotFoundException(String message) {
        super(message);
    }
}
