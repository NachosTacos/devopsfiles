package canary.paper;

import canary.paper.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("userRepository")
public interface UserRepo extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
