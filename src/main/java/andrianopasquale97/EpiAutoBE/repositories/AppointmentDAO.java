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
    @Query("SELECT a FROM Appointment a WHERE a.appointmentDate = :appointmentDate")
    List<Appointment> findAppointmentsByDate(@Param("appointmentDate") LocalDate appointmentDate);
}
