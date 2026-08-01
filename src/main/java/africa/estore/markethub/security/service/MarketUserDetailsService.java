package africa.estore.markethub.security.service;

import africa.estore.markethub.model.User;
import africa.estore.markethub.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@AllArgsConstructor
public class MarketUserDetailsService implements UserDetailsService {
    private final UserService userService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getUserBy(username);
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), Set.of(
                new SimpleGrantedAuthority("ROLE_USER")
        ));
    }
}
