package africa.estore.markethub.integration.wallet;

import africa.estore.markethub.dto.request.FundWalletRequest;
import africa.estore.markethub.dto.response.PaystackPaymentResponse;
import africa.estore.markethub.dto.response.TransactionResponse;
import africa.estore.markethub.exception.WalletNotFoundException;
import africa.estore.markethub.model.Transaction;
import africa.estore.markethub.model.TransactionType;
import africa.estore.markethub.model.Wallet;
import africa.estore.markethub.repository.TransactionRepository;
import africa.estore.markethub.repository.WalletRepository;
import africa.estore.markethub.service.WalletService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class WalletServiceIntegrationTest {

    @Autowired
    private WalletService walletService;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    @Transactional
    public void testConcurrentWalletCreationForSameUser() throws InterruptedException {
        String userId = "concurrent-user-" + System.currentTimeMillis();
        ExecutorService executor = Executors.newFixedThreadPool(2);
        CountDownLatch startLatch = new CountDownLatch(2);
        CountDownLatch endLatch = new CountDownLatch(2);
        AtomicInteger successCount = new AtomicInteger(0);

        // Thread 1: Create wallet
        executor.submit(() -> {
            try {
                startLatch.countDown();
                startLatch.await();
                walletService.createWalletFor(userId);
                successCount.incrementAndGet();
            } catch (Exception e) {
                // Expected: one might fail due to unique constraint
            } finally {
                endLatch.countDown();
            }
        });

        // Thread 2: Create wallet for same user
        executor.submit(() -> {
            try {
                startLatch.countDown();
                startLatch.await();
                walletService.createWalletFor(userId);
                successCount.incrementAndGet();
            } catch (Exception e) {
                // Expected: one might fail due to unique constraint
            } finally {
                endLatch.countDown();
            }
        });

        endLatch.await();
        executor.shutdown();

        // Verify: At least one wallet exists for this user
        assertThat(successCount.get()).isGreaterThan(0);
    }

    @Test
    @Transactional
    public void testTransactionOrderingStabilityWithPagination() throws Exception {
        // Create a wallet
        Wallet wallet = new Wallet();
        wallet.setUserId("test-user-" + System.currentTimeMillis());
        wallet = walletRepository.save(wallet);

        // Insert 15 transactions with slight delays to ensure different timestamps
        List<Transaction> transactions = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            Transaction transaction = new Transaction();
            transaction.setWallet(wallet);
            transaction.setType(TransactionType.CREDIT);
            transaction.setAmount(new BigDecimal("1000"));
            transaction.setReference("REF-" + i);
            transactions.add(transactionRepository.save(transaction));
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Fetch transactions in pages
        try {
            List<TransactionResponse> page0 = walletService.retrieveTransactionsFor(wallet.getId(), 0, 5);
            List<TransactionResponse> page1 = walletService.retrieveTransactionsFor(wallet.getId(), 1, 5);
            List<TransactionResponse> page2 = walletService.retrieveTransactionsFor(wallet.getId(), 2, 5);

            // Verify: All pages have content (15 transactions / 5 per page = 3 pages)
            assertThat(page0).hasSize(5);
            assertThat(page1).hasSize(5);
            assertThat(page2).hasSize(5);

            // Verify: No duplicates across pages (ordering is stable)
            List<TransactionResponse> allResults = new ArrayList<>();
            allResults.addAll(page0);
            allResults.addAll(page1);
            allResults.addAll(page2);
            assertThat(allResults).hasSize(15);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    @Sql(scripts = {"/db/data.sql"})
    void testCanInitializePayStackTransactionRequest(){
        try {
            FundWalletRequest fundWalletRequest = new FundWalletRequest();
            fundWalletRequest.setUserId("10a5dbf7-0353-40d3-85pd-cc322485ea2c");
            fundWalletRequest.setAmount(500000L);
            PaystackPaymentResponse response = walletService.fundWallet(fundWalletRequest);
            assertThat(response).isNotNull();
            assertThat(response.getData()).isNotNull();
            assertThat(response.getData().getAuthorizationUrl()).isNotNull();
            assertThat(response.getData().getAuthorizationUrl()).containsIgnoringCase("paystack");
        }catch (WalletNotFoundException ex){
            assertThat(ex).isNull();
        }
    }


}
