package com.flexbox.backend.webhook;

import com.stripe.exception.SignatureVerificationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/webhooks/stripe")
public class WebhookController {

    private final WebhookService webhookService;

    public WebhookController(WebhookService webhookService) {
        this.webhookService = webhookService;
    }

    @PostMapping
    public ResponseEntity<String> receiveEvent(@RequestBody String payload,
                                                @RequestHeader("Stripe-Signature") String signatureHeader) {
        try {
            com.stripe.model.Event event = webhookService.verifySignature(payload, signatureHeader);
            webhookService.handleEvent(event);
            return ResponseEntity.ok("received");
        } catch (SignatureVerificationException e) {
            return ResponseEntity.badRequest().body("invalid signature");
        }
    }
}
