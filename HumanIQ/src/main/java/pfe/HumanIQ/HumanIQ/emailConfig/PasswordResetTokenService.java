package pfe.HumanIQ.HumanIQ.emailConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pfe.HumanIQ.HumanIQ.models.ForgotPwd;
import pfe.HumanIQ.HumanIQ.repositories.PasswordResetTokenRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetTokenService {
    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    public String createPasswordResetToken(String username) {
        String token = UUID.randomUUID().toString();
        ForgotPwd resetToken = new ForgotPwd();
        resetToken.setToken(token);
        resetToken.setUsername(username);
        resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(10)); // 10 minutes de validité
        tokenRepository.save(resetToken);
        return token;
    }

    public Optional<ForgotPwd> getToken(String token) {
        return tokenRepository.findByToken(token);
    }

    public void deleteToken(ForgotPwd token) {
        tokenRepository.delete(token);
    }
    public boolean isTokenExpired(ForgotPwd token) {
        return token.getExpiryDate().isBefore(LocalDateTime.now());
    }

}
