package africa.estore.markethub.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransactionResponse {
    private String id;
    private String reference;
    private Long amount;
}
