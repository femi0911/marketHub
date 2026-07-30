package africa.estore.markethub.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhook")
@Slf4j
public class WebhookController {

    @PostMapping
    public void paystackWebhook(@RequestBody String payload, @RequestHeader(name = "x-paystack-signature") String hash) {
        log.info("Paystack Webhook Payload: {}", payload);
        log.info("Paystack Webhook hash: {}", hash);
    }
}
