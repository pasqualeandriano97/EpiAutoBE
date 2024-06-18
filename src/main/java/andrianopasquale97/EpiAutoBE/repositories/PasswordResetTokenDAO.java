package andrianopasquale97.EpiAutoBE.repositories;

import andrianopasquale97.EpiAutoBE.entities.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PasswordResetTokenDAO extends JpaRepository<PasswordResetToken, UUID> {

}
