package africa.estore.markethub.service;

import africa.estore.markethub.dto.response.TransactionResponse;
import africa.estore.markethub.dto.response.WalletResponse;
import africa.estore.markethub.exception.WalletNotFoundException;
import africa.estore.markethub.model.Wallet;
import africa.estore.markethub.repository.WalletRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class WalletServiceImpl implements WalletService {
    private final WalletRepository walletRepository;
    private final ModelMapper modelMapper;
    private final TransactionService transactionService;

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

    private Wallet getWalletWith(String walletId) throws WalletNotFoundException {
        return walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException(
                        String.format("wallet with id %s not found", walletId)));
    }
}
