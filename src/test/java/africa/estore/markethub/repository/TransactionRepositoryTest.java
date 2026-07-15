package africa.estore.markethub.repository;

import africa.estore.markethub.model.Transaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class TransactionRepositoryTest {
    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    @Sql(scripts = "/db/data.sql")
    void findByWalletIdTest() {
        final String walletId = "10a5dbf7-0353-40d3-92a1-cc322485ea2c";
        int page = 1;
        int size = 10;
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());
        Page<Transaction> transactions = transactionRepository.findByWalletId(walletId, pageable);
        assertThat(transactions.getContent()).hasSize(3);

    }

}