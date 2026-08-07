package com.flexbox.backend.webhook;

import com.flexbox.backend.order.CheckoutSession;
import com.flexbox.backend.order.CheckoutSessionRepository;
import com.flexbox.backend.order.CheckoutSessionStatus;
import com.flexbox.backend.order.Order;
import com.flexbox.backend.order.OrderRepository;
import com.flexbox.backend.order.OrderStatus;
import com.flexbox.backend.payment.Payment;
import com.flexbox.backend.payment.PaymentStatus;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.exception.SignatureVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Map;

@Service
public class WebhookService {

    private final String webhookSecret;
    private final WebhookEventRepository webhookEventRepository;
    private final CheckoutSessionRepository checkoutSessionRepository;
    private final OrderRepository orderRepository;

    public WebhookService(@Value("${stripe.api.webhook-secret}") String webhookSecret,
                           WebhookEventRepository webhookEventRepository,
                           CheckoutSessionRepository checkoutSessionRepository,
                           OrderRepository orderRepository) {
        this.webhookSecret = webhookSecret;
        this.webhookEventRepository = webhookEventRepository;
        this.checkoutSessionRepository = checkoutSessionRepository;
        this.orderRepository = orderRepository;
    }

    public com.stripe.model.Event verifySignature(String payload, String signatureHeader)
            throws SignatureVerificationException {
        return Webhook.constructEvent(payload, signatureHeader, webhookSecret);
    }

    @Transactional
    public void handleEvent(com.stripe.model.Event event) {
        WebhookEvent webhookEvent = webhookEventRepository.findByStripeEventId(event.getId())
                .orElseGet(() -> {
                    WebhookEvent newEvent = new WebhookEvent();
                    newEvent.setStripeEventId(event.getId());
                    newEvent.setEventType(event.getType());
                    newEvent.setPayload(Map.of("raw", event.toJson()));
                    newEvent.setIsProcessed(false);
                    newEvent.setReceivedAt(OffsetDateTime.now());
                    return webhookEventRepository.save(newEvent);
                });

        // Only skip if this event was already successfully processed. An
        // event that exists but never finished (isProcessed still false, from
        // a prior attempt that threw partway through) gets retried here
        // instead of silently skipped, since Stripe will keep resending it.
        if (Boolean.TRUE.equals(webhookEvent.getIsProcessed())) {
            return;
        }

        switch (event.getType()) {
            case "checkout.session.completed" -> handleCheckoutCompleted(event);
            case "checkout.session.expired" -> handleCheckoutExpired(event);
            case "payment_intent.payment_failed" -> handlePaymentFailed(event);
            default -> {
                // event type not handled, still logged above for audit purposes
            }
        }

        webhookEvent.setIsProcessed(true);
        webhookEvent.setProcessedAt(OffsetDateTime.now());
        webhookEventRepository.save(webhookEvent);
    }

    private void handleCheckoutCompleted(com.stripe.model.Event event) {
        Session stripeSession = (Session) event.getDataObjectDeserializer()
                .getObject()
                .orElseThrow(() -> new IllegalStateException("Could not deserialize checkout session"));

        CheckoutSession checkoutSession = checkoutSessionRepository
                .findByStripeSessionId(stripeSession.getId())
                .orElseThrow(() -> new IllegalStateException(
                        "No CheckoutSession found for Stripe session " + stripeSession.getId()));

        checkoutSession.setStatus(CheckoutSessionStatus.COMPLETE);

        Payment payment = checkoutSession.getPayment();
        payment.setStatus(PaymentStatus.SUCCEEDED);
        payment.setPaidAt(OffsetDateTime.now());
        payment.setStripePaymentIntentId(stripeSession.getPaymentIntent());

        Order order = payment.getOrder();
        order.setStatus(OrderStatus.COMPLETED);
        orderRepository.save(order);

        checkoutSessionRepository.save(checkoutSession);
    }

    private void handleCheckoutExpired(com.stripe.model.Event event) {
        Session stripeSession = (Session) event.getDataObjectDeserializer()
                .getObject()
                .orElseThrow(() -> new IllegalStateException("Could not deserialize checkout session"));

        checkoutSessionRepository.findByStripeSessionId(stripeSession.getId())
                .ifPresent(checkoutSession -> {
                    checkoutSession.setStatus(CheckoutSessionStatus.EXPIRED);
                    checkoutSessionRepository.save(checkoutSession);

                    Payment payment = checkoutSession.getPayment();
                    payment.setStatus(PaymentStatus.FAILED);

                    Order order = payment.getOrder();
                    order.setStatus(OrderStatus.CANCELLED);
                    orderRepository.save(order);
                });
    }

    private void handlePaymentFailed(com.stripe.model.Event event) {
        // Payment intent failures are already reflected through the checkout
        // session status once Stripe marks the session as expired. Logged via
        // the WebhookEvent record above for audit purposes.
    }
}
