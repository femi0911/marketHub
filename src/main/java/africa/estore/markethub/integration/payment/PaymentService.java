package africa.estore.markethub.integration.payment;

import africa.estore.markethub.dto.response.PaystackPaymentResponse;

public interface PaymentService {
    PaystackPaymentResponse initializeTransaction(String email, Long amount);
}
