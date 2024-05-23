package andrianopasquale97.EpiAutoBE.repositories;

import andrianopasquale97.EpiAutoBE.entities.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleDAO extends JpaRepository<Vehicle,String> {
    Optional<Vehicle> findByPlate(String plate);
    @Query("SELECT v FROM Vehicle v ORDER BY CASE WHEN v.imageUrl IS NULL THEN 1 ELSE 0 END, v.imageUrl DESC")
    Page<Vehicle> findAllOrderedByImageUrl(Pageable pageable);
    @Query("SELECT v FROM Vehicle v WHERE LOWER(v.brand) = LOWER(:brand)")
    List<Vehicle> findByBrand(@Param("brand") String brand);

}
