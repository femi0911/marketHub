package africa.estore.markethub.controller;


import africa.estore.markethub.exception.ResourceNotFoundException;
import africa.estore.markethub.integration.payment.PaystackPaymentVerifier;
import africa.estore.markethub.service.PayStackWebhookService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.ObjectMapper;

import java.util.Base64;

@RestController
@RequestMapping("/webhook")
@Slf4j
@AllArgsConstructor
public class WebhookController {
    private final PaystackPaymentVerifier paystackPaymentVerifier;
    private final PayStackWebhookService payStackWebhookService;

    @PostMapping
    public void paystackWebhook(@RequestBody String payload, @RequestHeader(name = "x-paystack-signature") String hash) throws ResourceNotFoundException {
        payStackWebhookService.updateTransaction(payload);
    }

}
