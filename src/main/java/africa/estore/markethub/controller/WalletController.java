package africa.estore.markethub.controller;

import africa.estore.markethub.dto.request.FundWalletRequest;
import africa.estore.markethub.dto.response.TransactionResponse;
import africa.estore.markethub.dto.response.WalletResponse;
import africa.estore.markethub.exception.WalletNotFoundException;
import africa.estore.markethub.service.WalletService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @PostMapping("/{userId}/wallet/fund")
    @ResponseStatus(HttpStatus.OK)
    public WalletResponse fundWallet(
            @PathVariable @NotBlank(message = "userId is required") String userId,
            @Valid @RequestBody FundWalletRequest request) {
        return walletService.createWalletFor(userId);
    }

    @GetMapping("/{userId}/wallet")
    public WalletResponse getWalletBalance(
            @PathVariable @NotBlank(message = "userId is required") String userId) {
        return walletService.createWalletFor(userId);
    }

    @GetMapping("/{userId}/wallet/transactions")
    public List<TransactionResponse> getTransactionHistory(
            @PathVariable @NotBlank(message = "userId is required") String userId,
            @RequestParam(defaultValue = "0") @Min(value = 0, message = "page must be >= 0") int page,
            @RequestParam(defaultValue = "10") @Min(value = 1, message = "size must be >= 1") int size) throws WalletNotFoundException {
        return walletService.retrieveTransactionsFor(userId, page, size);
    }
}
