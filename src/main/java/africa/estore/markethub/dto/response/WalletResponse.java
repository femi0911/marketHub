package africa.estore.markethub.dto.response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WalletResponse {
    private String userId;
    private String walletId;
    private String balance;
    private String createdAt;
    private String updatedAt;
}
