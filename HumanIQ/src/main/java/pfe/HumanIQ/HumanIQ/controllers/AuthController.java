package pfe.HumanIQ.HumanIQ.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pfe.HumanIQ.HumanIQ.models.authRequest;
import pfe.HumanIQ.HumanIQ.models.LoginDTO;
import pfe.HumanIQ.HumanIQ.models.User;
import pfe.HumanIQ.HumanIQ.services.JwtService;
import pfe.HumanIQ.HumanIQ.services.UserService;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Autowired
    public AuthController(UserService userService, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            System.out.println("Registering new user with email: " + user.getEmail());
            User createdUser = userService.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (Exception e) {
            System.err.println("User registration failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User creation failed: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginDTO> login(@RequestBody authRequest authRequest) {
        try {
            System.out.println("Login attempt for email: " + authRequest.getEmail());
            
            // Vérifier si l'utilisateur existe
            User user = userService.findByEmail(authRequest.getEmail())
                .orElseThrow(() -> {
                    System.err.println("User not found: " + authRequest.getEmail());
                    return new BadCredentialsException("User not found");
                });
            
            System.out.println("Found user in database: " + user.getEmail());

            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
            );

            if (authenticate.isAuthenticated()) {
                System.out.println("Authentication successful for user: " + authRequest.getEmail());
                String token = jwtService.generateToken(authRequest.getEmail());
                return ResponseEntity.ok(new LoginDTO(token));
            } else {
                System.err.println("Authentication failed for user: " + authRequest.getEmail());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginDTO("Invalid credentials"));
            }
        } catch (BadCredentialsException e) {
            System.err.println("Bad credentials for user " + authRequest.getEmail() + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginDTO("Invalid credentials"));
        } catch (Exception e) {
            System.err.println("Authentication error for user " + authRequest.getEmail() + ": " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new LoginDTO("Authentication failed"));
        }
    }
}
