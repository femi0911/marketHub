package africa.estore.markethub.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaystackWebhookPayload {
    private String event;
    private PaystackWebhookData data;
}
