package africa.estore.markethub.controller;

import africa.estore.markethub.dto.response.TransactionResponse;
import africa.estore.markethub.dto.response.WalletResponse;
import africa.estore.markethub.service.WalletService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class WalletControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final WalletService walletService = org.mockito.Mockito.mock(WalletService.class);

    @Test
    public void testGetTransactionHistoryWithNegativePage() throws Exception {
        mockMvc.perform(get("/api/v1/users/user-123/wallet/transactions")
                .param("page", "-1")
                .param("size", "10"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetTransactionHistoryWithZeroSize() throws Exception {
        mockMvc.perform(get("/api/v1/users/user-123/wallet/transactions")
                .param("page", "0")
                .param("size", "0"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetTransactionHistoryWithNegativeSize() throws Exception {
        mockMvc.perform(get("/api/v1/users/user-123/wallet/transactions")
                .param("page", "0")
                .param("size", "-10"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetTransactionHistoryWithValidParams() throws Exception {
        mockMvc.perform(get("/api/v1/users/user-123/wallet/transactions")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk());
    }

    @Test
    public void testFundWalletEndpointExists() throws Exception {
        mockMvc.perform(post("/api/v1/users/user-123/wallet/fund")
                .contentType("application/json")
                .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetWalletBalanceEndpointExists() throws Exception {
        mockMvc.perform(get("/api/v1/users/user-123/wallet"))
                .andExpect(status().isOk());
    }
}
