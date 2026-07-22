package africa.estore.markethub.dto.request;

import africa.estore.markethub.model.TransactionType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTransactionRequest {
    private Long amount;
    private TransactionType transactionType;
}
