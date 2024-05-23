package andrianopasquale97.EpiAutoBE.repositories;

import andrianopasquale97.EpiAutoBE.entities.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDAO extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    Optional<User> findBySurnameAndName(String surname, String name);

    boolean existsByEmail(String email);
    @Modifying
    @Query("DELETE FROM Appointment a WHERE a.user.id = :userId")
    void deleteAppointmentsByUserId(@Param("userId") Integer userId);
    @Modifying
    @Query("DELETE FROM Rent r WHERE r.user.id = :userId")
    void deleteRentsByUserId(@Param("userId") Integer userId);

    @Transactional
    default void deleteUserWithRelations(Integer userId) {
        deleteAppointmentsByUserId(userId);
        deleteRentsByUserId(userId);
        deleteById(userId);
    }
}
