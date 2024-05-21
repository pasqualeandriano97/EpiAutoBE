package andrianopasquale97.EpiAutoBE.repositories;

import andrianopasquale97.EpiAutoBE.entities.Rent;
import andrianopasquale97.EpiAutoBE.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RentDAO extends JpaRepository<Rent, Integer> {
    @Query("SELECT r FROM Rent r WHERE r.vehicle.plate = :plate")
    List<Rent> findByVehicle(@Param("plate") String plate);

    Page<Rent> findByUserId(int id, Pageable pageable);

    List<Rent> findByUserId(int id);

    @Query("SELECT r FROM Rent r WHERE r.startDate <= CURRENT_DATE AND r.endDate >= CURRENT_DATE")
    List<Rent> findActiveRentsToday();
    @Query("SELECT r FROM Rent r WHERE r.user.id = :userId AND r.startDate > CURRENT_DATE")
    List<Rent> findUpcomingRentsByUserId(@Param("userId") int userId);
    @Query("SELECT r FROM Rent r WHERE r.date = :date")
    List<Rent> findRentsByDate(@Param("date") LocalDate date);
}
