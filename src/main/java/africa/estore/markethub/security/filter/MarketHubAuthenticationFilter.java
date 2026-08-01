package africa.estore.markethub.security.filter;

import africa.estore.markethub.security.dto.request.AuthRequest;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@Component
public class MarketHubAuthenticationFilter extends OncePerRequestFilter {
    private final ObjectMapper objectMapper;
    private final AuthenticationManager authenticationManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        boolean isAuthenticationRequest = request.getServletPath().equals("/api/v1/auth/login");
        if (isAuthenticationRequest) {
            // TODO: 1. extract authentication credentials (username and password) from the request
            InputStream requestBodyStream = request.getInputStream();
            AuthRequest authRequest = objectMapper.readValue(requestBodyStream, AuthRequest.class);
            //TODO 2. send extracted authentication credentials (username and password) to the AuthenticationManager
            Authentication authentication = new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword());
            Authentication authenticationResult = authenticationManager.authenticate(authentication);
            SecurityContextHolder.getContext().setAuthentication(authenticationResult);
            Map<String, String> body = new HashMap<>();
            //TODO: replace uuid with jwt
            body.put("access_token", UUID.randomUUID().toString());
            response.setContentType("application/json");
            response.getOutputStream().write(objectMapper.writeValueAsBytes(body));
            response.flushBuffer();
        }
        filterChain.doFilter(request, response);
    }
}
