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
import java.util.UUID;

import static java.time.LocalTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WalletServiceTest {
    @Mock
    private WalletRepository walletRepository;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private WalletServiceImpl walletService;

    @Test
    public void testCanCreateWalletSuccessfully() {
        final String userId = "999639b8-cf73-4f56-8396-c3926656b043";
        Wallet savedWallet = new Wallet();
        savedWallet.setUserId(userId);
        when(walletRepository.save(any(Wallet.class))).thenReturn(savedWallet);
        WalletResponse walletResponse = new WalletResponse();
        walletResponse.setUserId(userId);
        walletResponse.setCreatedAt(now().toString());
        walletResponse.setUpdatedAt(now().toString());
        when(modelMapper.map(savedWallet, WalletResponse.class)).thenReturn(walletResponse);
        WalletResponse wallet = walletService.createWalletFor(userId);
        assertThat(wallet).isNotNull();
        assertThat(wallet.getCreatedAt()).isNotNull();
        assertThat(wallet.getUpdatedAt()).isNotNull();
        assertThat(wallet.getUserId()).isEqualTo(userId);
    }

    @Test
    public void testCanRetrieveTransactionsForWalletSuccessfully() throws WalletNotFoundException {
        final String walletId = "10a5dbf7-0353-40d3-92a1-cc322485ea2c";
        int page = 1;
        int size = 10;
        List<TransactionResponse> transactions = walletService.retrieveTransactionsFor(walletId, page, size);
        assertThat(transactions).isNotNull();
        assertThat(transactions).isNotEmpty();
        assertThat(transactions.size()).isEqualTo(5);
    }



}
