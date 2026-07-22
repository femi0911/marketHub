package africa.estore.markethub.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FundWalletRequest {
    @NotBlank(message = "userId is required")
    private String userId;

    @Positive(message = "amount must be greater than 0")
    private Long amount;
}
