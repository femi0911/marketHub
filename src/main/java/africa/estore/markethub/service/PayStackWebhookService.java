package africa.estore.markethub.service;

import africa.estore.markethub.dto.request.PaystackWebhookPayload;
import africa.estore.markethub.exception.ResourceNotFoundException;
import africa.estore.markethub.model.Transaction;
import africa.estore.markethub.model.TransactionStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
@AllArgsConstructor
public class PayStackWebhookService {
    private final TransactionService transactionService;
    private final WalletService walletService;
    private final ObjectMapper objectMapper;
    public void updateTransaction(String payload) throws ResourceNotFoundException {
        PaystackWebhookPayload paystackWebhookPayload =
                objectMapper.readValue(payload, PaystackWebhookPayload.class);
        String reference = paystackWebhookPayload.getData().getReference();
        Transaction transaction = transactionService.getTransactionBy(reference);
        if (paystackWebhookPayload.getData().getStatus().equalsIgnoreCase("success")) {
            transaction.setStatus(TransactionStatus.COMPLETED);
        }else {
            transaction.setStatus(TransactionStatus.FAILED);
        }
        transactionService.save(transaction);
    }
}
