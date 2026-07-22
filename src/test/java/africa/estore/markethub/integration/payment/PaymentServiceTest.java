package africa.estore.markethub.integration.payment;


import africa.estore.markethub.dto.response.PaystackPaymentResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PaymentServiceTest {
    @Autowired
    private PaymentService paymentService;

    @Test
    void testCanInitializePayStackTransaction(){
        String email = "test@email.com";
        Long amount = 500000L;
        PaystackPaymentResponse response = paymentService.initializeTransaction(email, amount);
        assertThat(response).isNotNull();
        assertThat(response.getData()).isNotNull();
        assertThat(response.getData().getAuthorizationUrl()).containsIgnoringCase("paystack");

    }
}
