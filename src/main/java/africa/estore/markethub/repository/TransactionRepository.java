package africa.estore.markethub.repository;

import africa.estore.markethub.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, String> {
    Page<Transaction> findByWalletId(String walletId, Pageable pageable);
}
