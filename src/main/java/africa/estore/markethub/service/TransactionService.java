package africa.estore.markethub.service;

import africa.estore.markethub.dto.request.CreateTransactionRequest;
import africa.estore.markethub.dto.response.TransactionResponse;
import africa.estore.markethub.exception.WalletNotFoundException;
import africa.estore.markethub.model.Wallet;

import java.util.List;

public interface TransactionService {
    List<TransactionResponse> getTransactionsBy(String walletId, int page, int size);

    TransactionResponse createTransaction(CreateTransactionRequest transactionRequest, Wallet wallet) throws WalletNotFoundException;
}
