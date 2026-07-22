package africa.estore.markethub.service;

import africa.estore.markethub.dto.request.FundWalletRequest;
import africa.estore.markethub.dto.response.PaystackPaymentResponse;
import africa.estore.markethub.dto.response.TransactionResponse;
import africa.estore.markethub.dto.response.WalletResponse;
import africa.estore.markethub.exception.WalletNotFoundException;
import africa.estore.markethub.model.Wallet;

import java.util.List;

public interface WalletService {
    WalletResponse createWalletFor(String userId);

    List<TransactionResponse> retrieveTransactionsFor(String walletId, int page, int size) throws WalletNotFoundException;

    PaystackPaymentResponse fundWallet(FundWalletRequest fundWalletRequest) throws WalletNotFoundException;
    WalletResponse getWallet(String walletId) throws WalletNotFoundException;
    Wallet getWalletWith(String walletId) throws WalletNotFoundException;
}
