package pfe.HumanIQ.HumanIQ.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pfe.HumanIQ.HumanIQ.models.ForgotPwd;

import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<ForgotPwd,Long> {
    Optional<ForgotPwd> findByToken(String token);
    Optional<ForgotPwd> findByUsername(String username);
}
