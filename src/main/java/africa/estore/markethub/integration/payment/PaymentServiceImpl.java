package africa.estore.markethub.integration.payment;

import africa.estore.markethub.config.PayStackConfig;
import africa.estore.markethub.dto.request.PaystackPaymentRequest;
import africa.estore.markethub.dto.response.PaystackPaymentResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static org.springframework.http.HttpMethod.POST;

@RestController
@Slf4j
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final RestTemplate restTemplate;
    private final PayStackConfig payStackConfig;

    @Override
    public PaystackPaymentResponse initializeTransaction(String email, Long amount) {
        PaystackPaymentRequest paymentRequest = new PaystackPaymentRequest(email, amount);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(payStackConfig.getKey());
        URI uri = URI.create(payStackConfig.getUrl());
        RequestEntity<PaystackPaymentRequest> request = new RequestEntity<>(paymentRequest, headers, POST, uri);
        ResponseEntity<PaystackPaymentResponse> response = restTemplate.postForEntity(uri, request, PaystackPaymentResponse.class);
        return response.getBody();
    }
}
