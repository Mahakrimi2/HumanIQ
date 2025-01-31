package pfe.HumanIQ.HumanIQ.services.serviceUser;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pfe.HumanIQ.HumanIQ.models.User;
import pfe.HumanIQ.HumanIQ.repositories.UserRepo;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImp implements UserService, UserDetailsService {

    private final UserRepo userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImp(UserRepo userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User createUser(User user) {
        // Vérifier si l'email existe déjà
        if (userRepository.findByUserName(user.getUsername()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }
        // Encoder le mot de passe avant de sauvegarder
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user) {
        User existingUser = findById(user.getId());
        if(existingUser != null){
            // Vérifier si le nouvel email n'est pas déjà utilisé par un autre utilisateur
            Optional<User> userWithEmail = userRepository.findByUserName(user.getUsername());
            if (userWithEmail.isPresent() && !userWithEmail.get().getId().equals(user.getId())) {
                throw new RuntimeException("Email already exists");
            }
            // Si le mot de passe a été modifié, l'encoder
            if (user.getPassword() != null && !user.getPassword().equals(existingUser.getPassword())) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            return userRepository.save(user);
        } else {
            throw new RuntimeException("User does not exist");
        }
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public Optional<User> findByUsername(String userName) {
        return userRepository.findByUserName(userName);
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        return userRepository.findByUserName(userName)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + userName));
    }
}
