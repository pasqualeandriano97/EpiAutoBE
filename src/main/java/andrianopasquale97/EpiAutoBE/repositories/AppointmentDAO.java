package andrianopasquale97.EpiAutoBE.repositories;

import andrianopasquale97.EpiAutoBE.entities.Appointment;
import andrianopasquale97.EpiAutoBE.entities.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentDAO extends JpaRepository<Appointment, Integer> {
    Optional<Appointment> findByVehicleAndDate(Vehicle vehicle, LocalDate date);
    @Query("SELECT a FROM Appointment a WHERE a.date = :date")
    List<Appointment> findAppointmentsByDate(@Param("date") LocalDate date);
    @Query("SELECT a FROM Appointment a WHERE a.user.id = :userId")
    List<Appointment> findAppointmentsByUserId(@Param("userId") int userId);
    @Query("SELECT a FROM Appointment a WHERE a.date = :today")
    List<Appointment> findAppointmentsByToday(@Param("today") LocalDate today);
}
