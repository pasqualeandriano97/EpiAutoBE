package andrianopasquale97.EpiAutoBE.repositories;

import andrianopasquale97.EpiAutoBE.entities.Maintenance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MaintenanceDAO extends JpaRepository<Maintenance, Integer> {

    @Query("SELECT m FROM Maintenance m WHERE m.startDate <= :date AND m.endDate >= :date")
    List<Maintenance> findByDateBetweenStartAndEndDate(@Param("date") LocalDate date);

   List<Maintenance>findByVehiclePlate(String plate);
}
