package africa.estore.markethub.repository;

import africa.estore.markethub.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
