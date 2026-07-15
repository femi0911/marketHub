package africa.estore.markethub.service;

import africa.estore.markethub.dto.response.TransactionResponse;
import africa.estore.markethub.dto.response.WalletResponse;
import africa.estore.markethub.exception.WalletNotFoundException;

import java.util.List;

public interface WalletService {
    WalletResponse createWalletFor(String userId);

    List<TransactionResponse> retrieveTransactionsFor(String walletId, int page, int size) throws WalletNotFoundException;
}
