package africa.estore.markethub.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @ManyToOne
    private Wallet wallet;
    private TransactionType type;
    private BigDecimal amount;
    private String reference;
    @CreationTimestamp
    private LocalDateTime createdAt;
    private TransactionStatus status;

    @PrePersist
    private void generateReference() {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        //TODO: replace magic numbers
        this.reference = uuid.substring(uuid.length() - 10, uuid.length() - 1);
    }

}
