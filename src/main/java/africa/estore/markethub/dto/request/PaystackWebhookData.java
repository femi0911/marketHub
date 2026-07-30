package africa.estore.markethub.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaystackWebhookData {
    private String status;
    private String reference;
    private Integer amount;
}
