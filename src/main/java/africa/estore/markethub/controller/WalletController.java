package africa.estore.markethub.controller;

import africa.estore.markethub.dto.request.FundWalletRequest;
import africa.estore.markethub.dto.response.PaystackPaymentResponse;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @PostMapping("/{userId}/wallet/fund")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PaystackPaymentResponse> fundWallet(
            @PathVariable @NotBlank(message = "userId is required") String userId,
            @Valid @RequestBody FundWalletRequest request) throws WalletNotFoundException {
        return ResponseEntity.ok(walletService.fundWallet(request));
    }

}
