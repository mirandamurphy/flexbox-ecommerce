package com.flexbox.backend.webhook;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = WebhookController.class)
class WebhookControllerTest {

    @Autowired
    MockMvcTester mockMvcTester;

    @MockitoBean
    WebhookService webhookService;

    @Test
    void receiveEvent_shouldReturn200_whenSignatureValid() throws Exception {
        Event fakeEvent = new Event();

        when(webhookService.verifySignature("{}", "valid_sig")).thenReturn(fakeEvent);
        doNothing().when(webhookService).handleEvent(fakeEvent);

        MvcTestResult result = mockMvcTester
                .post()
                .uri("/api/webhooks/stripe")
                .header("Stripe-Signature", "valid_sig")
                .content("{}")
                .exchange();

        assertThat(result).hasStatus(HttpStatus.OK);
        verify(webhookService).handleEvent(fakeEvent);
    }

    @Test
    void receiveEvent_shouldReturn400_whenSignatureInvalid() throws Exception {
        when(webhookService.verifySignature("{}", "bad_sig"))
                .thenThrow(new SignatureVerificationException("bad signature", "bad_sig"));

        MvcTestResult result = mockMvcTester
                .post()
                .uri("/api/webhooks/stripe")
                .header("Stripe-Signature", "bad_sig")
                .content("{}")
                .exchange();

        assertThat(result)
                .hasStatus(HttpStatus.BAD_REQUEST)
                .bodyText()
                .isEqualTo("invalid signature");
    }
}
