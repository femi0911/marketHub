package africa.estore.markethub.service;

import africa.estore.markethub.dto.request.CreateTransactionRequest;
import africa.estore.markethub.dto.response.TransactionResponse;
import africa.estore.markethub.exception.WalletNotFoundException;
import africa.estore.markethub.model.TransactionType;
import africa.estore.markethub.model.Wallet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static africa.estore.markethub.model.TransactionType.CREDIT;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TransactionServiceTest {
    @Autowired
    private TransactionService transactionService;

    @Test
    public void testCanRetrieveTransactionsForWallet() {
        final String walletId = "10a5dbf7-0353-40d3-92a1-cc322485ea2c";
        int page = 1;
        int size = 5;
        List<TransactionResponse>  transactions = transactionService.getTransactionsBy(walletId, page, size);
        assertThat(transactions).isNotEmpty();
        assertThat(transactions.size()).isEqualTo(3);
    }

    @Test
    void testCanCreateTransaction() {
        try {
            final String walletId = "10a5dbf7-0353-40d3-92a1-cc322485ea2c";
            Wallet wallet = new Wallet();
            wallet.setId(walletId);
            wallet.setUserId(UUID.randomUUID().toString());
            CreateTransactionRequest transactionRequest = new CreateTransactionRequest();
            transactionRequest.setAmount(50000L);
            transactionRequest.setTransactionType(CREDIT);
            TransactionResponse transactionResponse = transactionService.createTransaction(transactionRequest, wallet);
            assertThat(transactionResponse).isNotNull();
            assertThat(transactionResponse.getReference()).isNotNull();
        }catch (WalletNotFoundException ex){
            ex.printStackTrace();
        }
    }
}
