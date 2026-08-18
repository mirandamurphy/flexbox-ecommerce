package com.flexbox.backend.email;

import com.flexbox.backend.order.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;
    private final String fromAddress;

    public EmailService(JavaMailSender mailSender,
                         @Value("${app.mail.from}") String fromAddress) {
        this.mailSender = mailSender;
        this.fromAddress = fromAddress;
    }

    /**
     * Sends an order confirmation email. Failure to send does not roll back
     * or block the checkout flow itself, an order that completed
     * successfully in Stripe should not be undone just because the
     * confirmation email failed to go out. The failure is logged so it can
     * be investigated and, if needed, resent manually.
     */
    public void sendOrderConfirmation(Order order) {
        String recipientEmail = order.getUser().getEmail();
        String firstName = order.getUser().getFirstName();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(recipientEmail);
        message.setSubject("Your Flexbox order #" + order.getId() + " is confirmed");
        message.setText(buildBody(order, firstName));

        try {
            mailSender.send(message);
        } catch (MailException e) {
            log.error("Failed to send order confirmation email for order {}: {}",
                    order.getId(), e.getMessage());
        }
    }

    private String buildBody(Order order, String firstName) {
        String name = (firstName == null || firstName.isBlank()) ? "there" : firstName;

        return "Hi " + name + ",\n\n"
                + "Thanks for your order! Order #" + order.getId() + " has been confirmed.\n\n"
                + "Total: " + order.getCurrency() + " " + order.getTotalAmount() + "\n\n"
                + "You can view your order history any time from your account page.\n\n"
                + "- The Flexbox Team";
    }
}
