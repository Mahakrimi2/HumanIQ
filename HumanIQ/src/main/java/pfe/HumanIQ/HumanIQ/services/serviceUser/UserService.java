package pfe.HumanIQ.HumanIQ.services.serviceUser;

import org.springframework.security.core.userdetails.UserDetailsService;
import pfe.HumanIQ.HumanIQ.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {
    List<User> getAllUsers();
    User createUser(User user);
    User updateUser(User user);
    void deleteUser(Long id);
    User findById(Long id);
    Optional<User> findByUsername(String userName);
}
