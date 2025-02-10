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
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }
        // Encoder le mot de passe avant de sauvegarder
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(user.getPassword());
        newUser.setRole(user.getRole());

        newUser.setFullname(user.getFullname());  // Ajout de fullname
        newUser.setAddress(user.getAddress());    // Ajout de address
        newUser.setGender(user.getGender());      // Ajout de gender
        newUser.setPosition(user.getPosition());  // Ajout de position
        newUser.setTelNumber(user.getTelNumber()); // Ajout
        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user) {
        User existingUser = findById(user.getId());
        if(existingUser != null){
            // Vérifier si le nouvel email n'est pas déjà utilisé par un autre utilisateur
            Optional<User> userWithEmail = userRepository.findByUsername(user.getUsername());
            if (userWithEmail.isPresent() && !userWithEmail.get().getId().equals(user.getId())) {
                throw new RuntimeException("Email already exists");
            }

            // Si le mot de passe a été modifié, l'encoder
            if (user.getPassword() != null && !user.getPassword().equals(existingUser.getPassword())) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            } else {
                // Ne pas changer le mot de passe si aucun nouveau mot de passe n'est fourni
                user.setPassword(existingUser.getPassword());
            }

            // Mettre à jour les autres champs, même si ce sont des nouveaux
            existingUser.setUsername(user.getUsername());
            existingUser.setRole(user.getRole());

            // Mise à jour des champs additionnels
            existingUser.setFullname(user.getFullname() != null ? user.getFullname() : existingUser.getFullname());
            existingUser.setAddress(user.getAddress() != null ? user.getAddress() : existingUser.getAddress());
            existingUser.setGender(user.getGender() != null ? user.getGender() : existingUser.getGender());
            existingUser.setPosition(user.getPosition() != null ? user.getPosition() : existingUser.getPosition());
            existingUser.setTelNumber(user.getTelNumber() != null ? user.getTelNumber() : existingUser.getTelNumber());

            // Sauvegarder l'utilisateur mis à jour
            return userRepository.save(existingUser);
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
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
    }
}
