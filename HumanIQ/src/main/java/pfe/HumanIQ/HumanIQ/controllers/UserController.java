package pfe.HumanIQ.HumanIQ.controllers;

import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pfe.HumanIQ.HumanIQ.models.User;
import pfe.HumanIQ.HumanIQ.services.serviceAuth.JwtService;
import pfe.HumanIQ.HumanIQ.services.serviceUser.UserService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    
    
    private final JwtService jwtService;

    @Autowired
    public UserController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.ok(createdUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        User updatedUser = userService.updateUser(user);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        User user = userService.findById(id);
        return ResponseEntity.ok(user);
    }

   @PostMapping("/reset-password")
    public ResponseEntity<Object> resetPassword(@RequestParam String username) {
            Optional<User> userOptional=jwtService.findUserByEmail(username);
            if(!userOptional.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("msg", "user not found"));

            }
            User user=userOptional.get();
            String tempPassword=jwtService.generateTemporaryPassword();
            jwtService.hashAndSavePassword(user, tempPassword);

            try{
                jwtService.sendTemporaryPasswordEmail(user.getEmail(),tempPassword);
                return ResponseEntity.ok("Temporary password sent to email");

            }catch (MessagingException e){
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("failed to send email");
            }
   }

}
