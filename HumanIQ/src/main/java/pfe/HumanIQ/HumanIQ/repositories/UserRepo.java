package pfe.HumanIQ.HumanIQ.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pfe.HumanIQ.HumanIQ.models.User;
import java.util.Optional;

@Repository
@Transactional
public interface UserRepo extends JpaRepository<User,Long>{
    Optional<User> findByUserName(String userName);
}
