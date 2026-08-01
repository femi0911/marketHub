package africa.estore.markethub.security.config;

import africa.estore.markethub.security.filter.MarketHubAuthenticationFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@AllArgsConstructor
public class SecurityConfig {
    private final MarketHubAuthenticationFilter marketHubAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(c->c.disable());
        http.csrf(c->c.disable());
        http.addFilterAt(marketHubAuthenticationFilter, BasicAuthenticationFilter.class);
        http.authorizeHttpRequests(r->
                r.requestMatchers(HttpMethod.POST, "/api/v1/auth/login").permitAll());
        return http.build();
    }

}
