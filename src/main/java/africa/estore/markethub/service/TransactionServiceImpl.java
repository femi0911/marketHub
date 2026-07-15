package africa.estore.markethub.service;

import africa.estore.markethub.dto.response.TransactionResponse;
import africa.estore.markethub.model.Transaction;
import africa.estore.markethub.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final ModelMapper modelMapper;
    @Override
    public List<TransactionResponse> getTransactionsBy(String walletId, int page, int size) {
        if (page < 1) page = 1;
        if (size < 1 || size > 100) size = 10;
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());
        Page<Transaction> transactions = transactionRepository.findByWalletId(walletId, pageable);
        List<TransactionResponse> response = new ArrayList<>();
        transactions.getContent().forEach(transaction -> {
            response.add(modelMapper.map(transaction, TransactionResponse.class));
        });
        return response;
    }
}
