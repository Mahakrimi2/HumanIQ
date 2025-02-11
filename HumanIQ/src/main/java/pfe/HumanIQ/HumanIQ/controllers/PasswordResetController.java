package pfe.HumanIQ.HumanIQ.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pfe.HumanIQ.HumanIQ.config.AuthenticationConfig;
import pfe.HumanIQ.HumanIQ.emailConfig.EmailServiceImpl;
import pfe.HumanIQ.HumanIQ.emailConfig.PasswordResetTokenService;
import pfe.HumanIQ.HumanIQ.models.ForgotPwd;
import pfe.HumanIQ.HumanIQ.models.User;
import pfe.HumanIQ.HumanIQ.repositories.UserRepo;

import java.util.Optional;

@RestController
@RequestMapping("/api/password")
public class PasswordResetController {

    @Autowired
    private PasswordResetTokenService passwordResetTokenService;
    @Autowired
    private UserRepo userRepository;
    @Autowired
    private AuthenticationConfig authenticationConfig;
    @Autowired
    EmailServiceImpl emailService;

    //Générer un token et envoyer l'email
    @PostMapping("/forgot")
    public ResponseEntity<String>forgotpassword(@RequestParam String username){
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isEmpty()){
         return ResponseEntity.badRequest().body("User not found");
        }
        String token=passwordResetTokenService.createPasswordResetToken(username);
        emailService.sendPasswordResetEmail(username,token);
        return ResponseEntity.ok(token);
    }

    //vérifier le token
    @PostMapping("/validate-token")
    public ResponseEntity<String>validateToken(@RequestParam String token){
        Optional<ForgotPwd> resetToken=passwordResetTokenService.getToken(token);
        if(resetToken.isEmpty() || resetToken.get().isExpired()){
            return ResponseEntity.badRequest().body("Invalid token ou expiré");
        }
        return ResponseEntity.ok("token valide");
    }

    @PostMapping("/reset")
    public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        Optional<ForgotPwd> resetToken =passwordResetTokenService.getToken(token);
        if (resetToken.isEmpty() || resetToken.get().isExpired()) {
            return ResponseEntity.badRequest().body("Token invalide ou expiré.");
        }

        Optional<User> user = userRepository.findByUsername(resetToken.get().getUsername());
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body("Utilisateur non trouvé.");
        }

        user.get().setPassword(authenticationConfig.passwordEncoder().encode(newPassword));
        userRepository.save(user.get());

        // Supprimer le token après utilisation
        passwordResetTokenService.deleteToken(resetToken.get());

        return ResponseEntity.ok("Mot de passe réinitialisé avec succès.");
    }
}






