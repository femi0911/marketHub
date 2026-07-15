package africa.estore.markethub.service;

import africa.estore.markethub.dto.response.TransactionResponse;

import java.util.List;

public interface TransactionService {
    List<TransactionResponse> getTransactionsBy(String walletId, int page, int size);
}
