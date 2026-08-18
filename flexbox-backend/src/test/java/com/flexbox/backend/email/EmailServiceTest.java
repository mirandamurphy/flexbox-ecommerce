package com.flexbox.backend.email;

import com.flexbox.backend.order.Order;
import com.flexbox.backend.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    private EmailService emailService;

    private Order buildOrder() {
        User user = new User();
        user.setEmail("customer@flexbox.ca");
        user.setFirstName("Jordan");

        Order order = new Order();
        order.setId(101L);
        order.setUser(user);
        order.setCurrency("CAD");
        order.setTotalAmount(new BigDecimal("59.98"));
        return order;
    }

    @Test
    void sendOrderConfirmation_sendsCorrectlyAddressedMessage() {
        emailService = new EmailService(mailSender, "no-reply@flexbox.ca");
        Order order = buildOrder();

        emailService.sendOrderConfirmation(order);

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(captor.capture());

        SimpleMailMessage sent = captor.getValue();
        assertThat(sent.getTo()).containsExactly("customer@flexbox.ca");
        assertThat(sent.getFrom()).isEqualTo("no-reply@flexbox.ca");
        assertThat(sent.getSubject()).contains("101");
        assertThat(sent.getText()).contains("Jordan");
        assertThat(sent.getText()).contains("59.98");
    }

    @Test
    void sendOrderConfirmation_fallsBackToGenericGreeting_whenNameMissing() {
        emailService = new EmailService(mailSender, "no-reply@flexbox.ca");
        Order order = buildOrder();
        order.getUser().setFirstName(null);

        emailService.sendOrderConfirmation(order);

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(captor.capture());
        assertThat(captor.getValue().getText()).contains("Hi there");
    }

    @Test
    void sendOrderConfirmation_doesNotThrow_whenMailServerUnavailable() {
        emailService = new EmailService(mailSender, "no-reply@flexbox.ca");
        Order order = buildOrder();

        doThrow(new MailSendException("SMTP server unavailable")).when(mailSender).send(any(SimpleMailMessage.class));

        // A failed email must never propagate back into the webhook flow,
        // an order that completed successfully in Stripe should not be
        // undone just because the confirmation email failed to send.
        assertThatCode(() -> emailService.sendOrderConfirmation(order)).doesNotThrowAnyException();
    }
}
