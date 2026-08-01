package africa.estore.markethub.security.manager;

import africa.estore.markethub.security.exception.UnsupportedAuthenticationTypeException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@AllArgsConstructor
@Slf4j
public class MarketHubAuthenticationManager implements AuthenticationManager {
    private final Set<AuthenticationProvider> providers;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        for (AuthenticationProvider provider : providers) {
            log.info("Authentication provider {}", provider);
            if (provider.supports(UsernamePasswordAuthenticationToken.class)){
                return provider.authenticate(authentication);
            }
        }
        throw new UnsupportedAuthenticationTypeException("auth type not supported");
    }
}
