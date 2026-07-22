package africa.estore.markethub.dto.response;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PaystackPaymentResponse {
        private Boolean status;
        private String message;
        private PaystackData data;
}
