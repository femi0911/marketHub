package africa.estore.markethub.service;

import africa.estore.markethub.dto.response.TransactionResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

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
}
