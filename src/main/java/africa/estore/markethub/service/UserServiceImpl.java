package africa.estore.markethub.service;

import africa.estore.markethub.model.User;
import africa.estore.markethub.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    //TODO: create custom exception
    @Override
    public User getUser(String userId) {
        return userRepository.findById(userId).orElseThrow(()-> new RuntimeException("User not found"));
    }
}
