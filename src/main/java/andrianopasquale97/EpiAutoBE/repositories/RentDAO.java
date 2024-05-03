package andrianopasquale97.EpiAutoBE.repositories;

import andrianopasquale97.EpiAutoBE.entities.Rent;
import andrianopasquale97.EpiAutoBE.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentDAO extends JpaRepository<Rent, Integer> {
    List<Rent> findByVehicle(String plate);

    Page<Rent> findByUserId(int id, Pageable pageable);

    @Query("SELECT r FROM Rent r WHERE r.startDate <= CURRENT_DATE AND r.endDate >= CURRENT_DATE")
    List<Rent> findActiveRentsToday();
    @Query("SELECT r FROM Rent r WHERE r.user.id = :userId AND r.startDate > CURRENT_DATE")
    List<Rent> findUpcomingRentsByUserId(@Param("userId") int userId);
}
