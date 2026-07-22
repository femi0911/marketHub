package africa.estore.markethub.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class PaystackPaymentRequest {
    private String email;
    private Long amount;
}
