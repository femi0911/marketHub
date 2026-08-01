package africa.estore.markethub.security;

import africa.estore.markethub.security.dto.request.AuthRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    public void testCanAuthenticateUser() throws Exception {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername("admin@email.com");
        authRequest.setPassword("password");
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/login")
               .content(objectMapper.writeValueAsString(authRequest)))
               .andExpect(status().is2xxSuccessful())
               .andDo(print());

    }
}
