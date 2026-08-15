package com.flexbox.backend.webhook;

import com.flexbox.backend.TestcontainersConfiguration;
import com.flexbox.backend.order.CheckoutSession;
import com.flexbox.backend.order.CheckoutSessionMode;
import com.flexbox.backend.order.CheckoutSessionRepository;
import com.flexbox.backend.order.CheckoutSessionStatus;
import com.flexbox.backend.order.Order;
import com.flexbox.backend.order.OrderRepository;
import com.flexbox.backend.order.OrderStatus;
import com.flexbox.backend.payment.Payment;
import com.flexbox.backend.payment.PaymentRepository;
import com.flexbox.backend.payment.PaymentStatus;
import com.flexbox.backend.user.User;
import com.stripe.Stripe;
import com.stripe.model.Event;
import com.stripe.net.ApiResource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
@Transactional
class WebhookServiceIntegrationTest {

    @Autowired
    private WebhookService webhookService;

    @Autowired
    private WebhookEventRepository webhookEventRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private CheckoutSessionRepository checkoutSessionRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private Order testOrder;
    private Payment testPayment;
    private CheckoutSession testCheckoutSession;

    private static final String STRIPE_SESSION_ID = "cs_test_webhook_123";
    private static final String STRIPE_PAYMENT_INTENT_ID = "pi_test_webhook_456";

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setEmail("webhook-test@flexbox.ca");
        user.setPasswordHash("dummy-hash");
        user.setFirstName("Test");
        user.setLastName("User");
        user.setPhoneNumber("1234567890");
        user.setIsEnabled(true);
        entityManager.persist(user);

        testOrder = new Order();
        testOrder.setUser(user);
        testOrder.setCurrency("CAD");
        testOrder.setTotalAmount(new BigDecimal("29.99"));
        testOrder.setStatus(OrderStatus.PENDING);
        entityManager.persist(testOrder);

        testPayment = new Payment();
        testPayment.setOrder(testOrder);
        testPayment.setAmount(new BigDecimal("29.99"));
        testPayment.setCurrency("CAD");
        testPayment.setStatus(PaymentStatus.PENDING);
        entityManager.persist(testPayment);

        testCheckoutSession = new CheckoutSession();
        testCheckoutSession.setUser(user);
        testCheckoutSession.setStripeSessionId(STRIPE_SESSION_ID);
        testCheckoutSession.setPayment(testPayment);
        testCheckoutSession.setMode(CheckoutSessionMode.PAYMENT);
        testCheckoutSession.setStatus(CheckoutSessionStatus.OPEN);
        testCheckoutSession.setAmountSubtotal(new BigDecimal("29.99"));
        testCheckoutSession.setAmountTotal(new BigDecimal("29.99"));
        testCheckoutSession.setCurrency("CAD");
        entityManager.persist(testCheckoutSession);

        entityManager.flush();
    }

    private Event buildEvent(String eventId, String eventType, String sessionId, String paymentIntentId) {
        String json = """
                {
                  "id": "%s",
                  "object": "event",
                  "api_version": "%s",
                  "type": "%s",
                  "data": {
                    "object": {
                      "id": "%s",
                      "object": "checkout.session",
                      "payment_intent": "%s"
                    }
                  }
                }
                """.formatted(eventId, Stripe.API_VERSION, eventType, sessionId, paymentIntentId);
        return ApiResource.GSON.fromJson(json, Event.class);
    }

    @Test
    void checkoutSessionCompleted_marksPaymentSucceededAndOrderCompleted() {
        Event event = buildEvent("evt_test_1", "checkout.session.completed", STRIPE_SESSION_ID, STRIPE_PAYMENT_INTENT_ID);

        webhookService.handleEvent(event);

        Order updatedOrder = orderRepository.findById(testOrder.getId()).orElseThrow();
        Payment updatedPayment = paymentRepository.findById(testPayment.getId()).orElseThrow();
        CheckoutSession updatedSession = checkoutSessionRepository.findById(testCheckoutSession.getId()).orElseThrow();

        assertThat(updatedOrder.getStatus()).isEqualTo(OrderStatus.COMPLETED);
        assertThat(updatedPayment.getStatus()).isEqualTo(PaymentStatus.SUCCEEDED);
        assertThat(updatedPayment.getStripePaymentIntentId()).isEqualTo(STRIPE_PAYMENT_INTENT_ID);
        assertThat(updatedSession.getStatus()).isEqualTo(CheckoutSessionStatus.COMPLETE);
    }

    @Test
    void checkoutSessionExpired_marksPaymentFailedAndOrderCancelled() {
        Event event = buildEvent("evt_test_2", "checkout.session.expired", STRIPE_SESSION_ID, STRIPE_PAYMENT_INTENT_ID);

        webhookService.handleEvent(event);

        Order updatedOrder = orderRepository.findById(testOrder.getId()).orElseThrow();
        Payment updatedPayment = paymentRepository.findById(testPayment.getId()).orElseThrow();
        CheckoutSession updatedSession = checkoutSessionRepository.findById(testCheckoutSession.getId()).orElseThrow();

        assertThat(updatedOrder.getStatus()).isEqualTo(OrderStatus.CANCELLED);
        assertThat(updatedPayment.getStatus()).isEqualTo(PaymentStatus.FAILED);
        assertThat(updatedSession.getStatus()).isEqualTo(CheckoutSessionStatus.EXPIRED);
    }

    @Test
    void duplicateEvent_isProcessedOnlyOnce() {
        Event event = buildEvent("evt_test_duplicate", "checkout.session.completed", STRIPE_SESSION_ID, STRIPE_PAYMENT_INTENT_ID);

        webhookService.handleEvent(event);
        webhookService.handleEvent(event);

        long count = webhookEventRepository.findByStripeEventId("evt_test_duplicate")
                .stream()
                .count();
        assertThat(count).isEqualTo(1);

        Order updatedOrder = orderRepository.findById(testOrder.getId()).orElseThrow();
        assertThat(updatedOrder.getStatus()).isEqualTo(OrderStatus.COMPLETED);
    }
}
