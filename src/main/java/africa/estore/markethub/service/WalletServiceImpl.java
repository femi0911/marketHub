package africa.estore.markethub.service;

import africa.estore.markethub.dto.request.CreateTransactionRequest;
import africa.estore.markethub.dto.request.FundWalletRequest;
import africa.estore.markethub.dto.response.PaystackPaymentResponse;
import africa.estore.markethub.dto.response.TransactionResponse;
import africa.estore.markethub.dto.response.WalletResponse;
import africa.estore.markethub.exception.WalletNotFoundException;
import africa.estore.markethub.integration.payment.PaymentService;
import africa.estore.markethub.model.TransactionType;
import africa.estore.markethub.model.User;
import africa.estore.markethub.model.Wallet;
import africa.estore.markethub.repository.WalletRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

import static africa.estore.markethub.model.TransactionType.CREDIT;

@AllArgsConstructor
@Service
public class WalletServiceImpl implements WalletService {
    private final WalletRepository walletRepository;
    private final ModelMapper modelMapper;
    @Lazy
    private final TransactionService transactionService;
    private final UserService userService;
    private final PaymentService paymentService;

    @Override
    public WalletResponse createWalletFor(String userId) {
        Wallet wallet = new Wallet();
        wallet.setUserId(userId);
        return modelMapper.map(walletRepository.save(wallet), WalletResponse.class);
    }

    @Override
    public List<TransactionResponse> retrieveTransactionsFor(String walletId, int page, int size) throws WalletNotFoundException {
        Wallet wallet = getWalletWith(walletId);
        return transactionService.getTransactionsBy(wallet.getId(), page, size);
    }


    @Override
    public PaystackPaymentResponse fundWallet(FundWalletRequest fundWalletRequest) throws WalletNotFoundException {
        User user = userService.getUser(fundWalletRequest.getUserId());
        Wallet wallet = user.getWallet();
        CreateTransactionRequest transactionRequest = modelMapper.map(fundWalletRequest, CreateTransactionRequest.class);
        transactionRequest.setTransactionType(CREDIT);
        TransactionResponse transaction = transactionService.createTransaction(transactionRequest, wallet);
        return paymentService.initializeTransaction(user.getEmail(), transaction.getAmount());
    }

    @Override
    public WalletResponse getWallet(String walletId) throws WalletNotFoundException {
        return modelMapper.map(getWalletWith(walletId), WalletResponse.class);
    }


    public Wallet getWalletWith(String walletId) throws WalletNotFoundException {
        return walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException(
                        String.format("wallet with id %s not found", walletId)));
    }
}
