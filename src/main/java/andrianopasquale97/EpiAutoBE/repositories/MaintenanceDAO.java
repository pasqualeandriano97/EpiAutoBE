package andrianopasquale97.EpiAutoBE.repositories;

import andrianopasquale97.EpiAutoBE.entities.Maintenance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaintenanceDAO extends JpaRepository<Maintenance, Integer> {

}
