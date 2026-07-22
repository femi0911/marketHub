package africa.estore.markethub.service;

import africa.estore.markethub.dto.response.TransactionResponse;
import africa.estore.markethub.dto.response.WalletResponse;
import africa.estore.markethub.exception.WalletNotFoundException;
import africa.estore.markethub.model.Wallet;
import africa.estore.markethub.repository.WalletRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WalletServiceTest {
    @Mock
    private WalletRepository walletRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private TransactionService transactionService;
    @InjectMocks
    private WalletServiceImpl walletService;

    @Test
    public void testCanCreateWalletSuccessfully() {
        final String userId = "10a5dbf7-0353-40d3-92a1-cc322485ea2c";
        Wallet savedWallet = new Wallet();
        savedWallet.setUserId(userId);
        when(walletRepository.save(any(Wallet.class))).thenReturn(savedWallet);
        WalletResponse walletResponse = new WalletResponse();
        walletResponse.setUserId(userId);
        when(modelMapper.map(savedWallet, WalletResponse.class)).thenReturn(walletResponse);
        WalletResponse wallet = walletService.createWalletFor(userId);
        assertThat(wallet).isNotNull();
        assertThat(wallet.getUserId()).isEqualTo(userId);
    }

    @Test
    public void testRetrieveTransactionsSuccessfully() throws WalletNotFoundException {
        final String walletId = "10a5dbf7-0353-40d3-92a1-cc322485ea2c";
        Wallet wallet = new Wallet();
        wallet.setId(walletId);
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));
        List<TransactionResponse> expected = List.of(new TransactionResponse(), new TransactionResponse());
        when(transactionService.getTransactionsBy(walletId, 0, 10)).thenReturn(expected);
        assertThat(walletService.retrieveTransactionsFor(walletId, 0, 10)).isEqualTo(expected);
    }

    @Test
    public void testRetrieveTransactionsThrowsWalletNotFound() {
        when(walletRepository.findById("nonexistent-wallet")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> walletService.retrieveTransactionsFor("nonexistent-wallet", 0, 10))
                .isInstanceOf(WalletNotFoundException.class)
                .hasMessageContaining("nonexistent-wallet");
    }

    @Test
    public void testRetrieveTransactionsReturnsEmptyPageGracefully() throws WalletNotFoundException {
        final String walletId = "10a5dbf7-0353-40d3-92a1-cc322485ea2c";
        Wallet wallet = new Wallet();
        wallet.setId(walletId);
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));
        when(transactionService.getTransactionsBy(walletId, 100, 10)).thenReturn(List.of());
        List<TransactionResponse> result = walletService.retrieveTransactionsFor(walletId, 100, 10);
        assertThat(result).isEmpty();
    }

    @Test
    public void testRetrieveTransactionsWithZeroTransactions() throws WalletNotFoundException {
        final String walletId = "10a5dbf7-0353-40d3-92a1-cc322485ea2c";
        Wallet wallet = new Wallet();
        wallet.setId(walletId);
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));
        when(transactionService.getTransactionsBy(walletId, 0, 10)).thenReturn(List.of());
        List<TransactionResponse> result = walletService.retrieveTransactionsFor(walletId, 0, 10);
        assertThat(result).isNotNull().isEmpty();
    }

    @Test
    public void testRetrieveTransactionsWithVeryLargePaginationParams() throws WalletNotFoundException {
        Wallet wallet = new Wallet();
        wallet.setId("wallet-large");
        when(walletRepository.findById("wallet-large")).thenReturn(Optional.of(wallet));

        List<TransactionResponse> maxTransactions = new java.util.ArrayList<>();
        for (int counter = 0; counter < 100; counter++) {
            maxTransactions.add(new TransactionResponse());
        }
        when(transactionService.getTransactionsBy("wallet-large", 1000000, 100000)).thenReturn(maxTransactions);

        List<TransactionResponse> result = walletService.retrieveTransactionsFor("wallet-large", 1000000, 100000);
        assertThat(result).isNotNull();
        assertThat(result).hasSize(100);
        assertThat(result.size()).isLessThanOrEqualTo(100);
    }
}
